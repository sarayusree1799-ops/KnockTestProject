package selenium_shutterbug.knock.api.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.knock.api.plat.client.Community;
import selenium_shutterbug.knock.utils.AssertionUtils;
import selenium_shutterbug.knock.utils.AuthType;
import selenium_shutterbug.knock.utils.SkipWithReducedStackTrace;
import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.config.XmlConfig;
import io.restassured.response.Response;
import lombok.Getter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.awaitility.Awaitility;
import org.json.JSONArray;
import org.json.JSONObject;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.InstanceProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClientBuilder;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;
import software.amazon.awssdk.services.sts.model.AssumeRoleResponse;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static io.restassured.path.json.JsonPath.from;

@Getter
public class BaseTest extends WebDriverBase {
    private static final Logger logger = LogManager.getLogger(BaseTest.class);
    private Response response = null;

    public static String lmaToken;
    public static String oAuth2Token;
    private static String userAccessToken;
    private static final HashMap<Integer, String> accessTokens = new HashMap<>();

    public BaseTest() {
        super();
    }

    public static String getoAuth2Token() {
        return oAuth2Token;
    }

    public static void setoAuth2Token(String oAuthToken) {
        oAuth2Token = oAuthToken;
    }

    public static String getLmaToken() {
        return lmaToken;
    }

    public static String getUserAccessToken() {
        return userAccessToken;
    }

    public static void setUserAccessToken(String userToken) {
        userAccessToken = userToken;
    }

    public static void setLmaToken(String setLmaToken) {
        lmaToken = setLmaToken;
    }

    public static void putInAccessToken(Integer key, String value) {
        accessTokens.put(key, value);
    }

    public static String getAccessToken(Integer key) {
        return accessTokens.get(key);
    }

    public String oAuth2Token() {
        String accessTokenUrl = BaseTest.context.getBean("accessTokenUrl", String.class);
        String clientID = BaseTest.context.getBean("client_id").toString();
        String clientSecret = BaseTest.context.getBean("client_secret").toString();
        String grantType = BaseTest.context.getBean("grant_type").toString();
        Response accessTokenResponse = (Response) RestAssured.given().formParam("Client_ID", clientID)
                .formParam("Client_Secret", clientSecret).formParam("grant_type", grantType).post(accessTokenUrl);
        setoAuth2Token(from(accessTokenResponse.asString()).get("access_token"));
        return from(accessTokenResponse.asString()).get("access_token");
    }

    public Response postMethod(String baseURL, String endpointUrl, String bodyString, String authType, int statusCode, List<Object> headerValue) {
        boolean apiCallStatus = true;
        HashMap<String, Object> map = headerMap(authType, headerValue);
        try {
            switch (authType.toUpperCase()) {
                case "REALPAGE":
                    this.response = (Response) RestAssured.given().config(RestAssuredConfig.config()
                                    .xmlConfig(XmlConfig.xmlConfig()
                                            .with()
                                            .namespaceAware(true)
                                            .declareNamespace("s", "http://schemas.xmlsoap.org/soap/envelope/")))
                            .baseUri(baseURL)
                            .headers(map)
                            .contentType("text/xml; charset=utf-8")
                            .body(bodyString)
                            .when()
                            .post();
                    break;
                case "YARDI":
                    this.response = (Response) RestAssured.given().config(RestAssuredConfig.config()
                                    .sslConfig(new SSLConfig().relaxedHTTPSValidation())
                                    .xmlConfig(XmlConfig.xmlConfig()
                                            .with()
                                            .namespaceAware(true)
                                            .declareNamespace("s", "http://schemas.xmlsoap.org/soap/envelope/")))
                            .baseUri(baseURL)
                            .contentType("text/xml; charset=utf-8")
                            .body(bodyString)
                            .when()
                            .post();
                    break;
                default:
                    this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).body(bodyString).when().post(endpointUrl);
                    break;
            }
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", getResponseInString(this.response.getBody().asString()));
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Payload: " + bodyString
                    + " Auth Token: " + headerValue.get(0) + " Actual Response Body: " + getResponseInString(this.response.getBody().asString())
                    + "Actual Status Code: " + this.response.statusCode()
                    + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public Response putMethod(String baseURL, String endpointUrl, String bodyString, String authType, int statusCode, List<Object> headerValue) {
        boolean apiCallStatus = true;
        HashMap<String, Object> map = headerMap(authType, headerValue);
        try {
            this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).body(bodyString).when().put(endpointUrl);
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", this.response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Payload: " + bodyString
                    + " Auth Token: " + headerValue.get(0) + " Actual Response Body: " + this.response.getBody().asString()
                    + " Actual Status Code: " + this.response.statusCode()
                    + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public Response patchMethod(String baseURL, String endpointUrl, String bodyString, String authType, int statusCode, List<Object> headerValue) {
        boolean apiCallStatus = true;
        HashMap<String, Object> map = headerMap(authType, headerValue);
        try {
            this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).body(bodyString).when().patch(endpointUrl);
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", this.response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Payload: " + bodyString
                    + " Auth Token: " + headerValue.get(0) + " Actual Response Body: " + this.response.getBody().asString()
                    + "Actual Status Code: " + this.response.statusCode()
                    + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public Response getMethod(String baseURL, String endpointUrl, String authType, int statusCode, List<Object> headerValue) {
        boolean apiCallStatus = true;
        HashMap<String, Object> map = headerMap(authType, headerValue);
        try {
            this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).when().get(endpointUrl);
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", this.response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Auth Token: " + headerValue.get(0)
                    + " Actual Response Body: " + this.response.getBody().asString() + " Actual Status Code: "
                    + this.response.statusCode() + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public Response deleteWithToken(String baseURL, String endpointUrl, String bodyString, String authType, int statusCode, List<Object> headerValue) {
        boolean apiCallStatus = true;
        HashMap<String, Object> map = headerMap(authType, headerValue);
        try {
            switch (authType) {
                case "XAUTHTOKEN":
                    this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).body(bodyString).when().delete(endpointUrl);
                    break;
                default:
                    if (!bodyString.isEmpty()) {
                        this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).body(bodyString).when().delete(endpointUrl);
                        break;
                    } else {
                        this.response = (Response) RestAssured.given().baseUri(baseURL).headers(map).when().delete(endpointUrl);
                        break;
                    }
            }
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", this.response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Payload: " + bodyString
                    + " Auth Token: " + headerValue.get(0) + " Actual Status Code: " + this.response.statusCode()
                    + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public HashMap<String, Object> headerMap(String authType, List<Object> headerValue) {
        HashMap<String, Object> header = new HashMap<>();
        switch (authType.toUpperCase()) {
            case "APIKEY":
                header.put("x-api-key", headerValue.get(0));
                header.put("Content-Type", "application/json");
                break;
            case "AUTHTOKEN":
                header.put("Authorization", "Bearer " + headerValue.get(0));
                header.put("Content-Type", "application/json");
                break;
            case "INTERNALAUTHTOKEN":
                header.put("Internal-Authorization", "Bearer " + headerValue.get(0));
                header.put("Content-Type", "application/json");
                break;
            case "XAUTHTOKEN":
                header.put("x-internal-authorization", "Bearer " + headerValue.get(0));
                header.put("Content-Type", "application/json");
                break;
            case "APPFOLIO":
                header.put("Authorization", "Basic " + headerValue.get(0));
                header.put("X-AppFolio-Partner-ID", headerValue.get(1));
                break;
            case "LEASINGTEAMAUTHTOKEN":
                header.put("authorization", "Bearer " + headerValue.get(0));
                header.put("x-knock-auth-as-leasing-team", headerValue.get(1));
                header.put("Content-Type", "application/json");
                break;
            case "REALPAGE":
                header.put("SOAPAction", headerValue.get(0));
                break;
            case "Yardi":
                header.put("Cookie", "BIGipServerp8223tp7s7dev80=2705307914.20480.0000");
                break;
            case "SYNDICATION_WITH_RPX_KEY":
                header.put("x-consumer-custom-id", headerValue.get(0));
                header.put("x-api-key", headerValue.get(1));
                header.put("Content-Type", "application/json");
                break;
            case "SYNDICATION_WITH_OUT_RPX_KEY":
                header.put("x-consumer-custom-id", headerValue.get(0));
                header.put("Content-Type", "application/json");
                break;
            case "FORM_PARAMS":
                header.put("Content-Type", "application/x-www-form-urlencoded");
                break;
            case "DOORWAY":
                header.put("x-doorway-property-id", headerValue.get(0));
                header.put("x-doorway-application-id", headerValue.get(1));
                header.put("x-doorway-profile-token", headerValue.get(2));
                break;
            case "BASICAUTH":
                header.put("Authorization", "Basic " + headerValue.get(0));
                break;
            default:
                header.put("Content-Type", "application/json");
                break;
        }
        return header;
    }

    public String getResponseInString(String responseAsString) {
        String escaped = StringEscapeUtils.escapeHtml4(responseAsString.trim());
        String actualResponseInString = "";
        if (responseAsString.trim().startsWith("{") || responseAsString.trim().startsWith("[")) {
            // JSON formatting
            actualResponseInString = "<code>" + escaped + "</code>";
        } else if (responseAsString.trim().startsWith("<")) {
            // XML formatting
            actualResponseInString = "<pre>" + escaped + "</pre>";
        } else {
            // Fallback
            actualResponseInString = "<p>" + escaped + "</p>";
        }
        return actualResponseInString;
    }

    public String readPayloadFromJson(String payloadJson) {
        String requestPayload = "";
        try {
            File file = new File(System.getProperty("user.dir") + "/TestData/" + payloadJson);
            requestPayload = FileUtils.readFileToString(file, String.valueOf(StandardCharsets.UTF_8));
        } catch (IOException e) {

        }
        return requestPayload;
    }

    public static String replaceStringValuesUsingMap(String xmlString, Map<String, String> stringReplacementMap) {
        for (Map.Entry<String, String> stringMap : stringReplacementMap.entrySet()) {
            xmlString = xmlString.replace(stringMap.getKey(), stringMap.getValue());
        }
        return xmlString;
    }

    public void explicitWait(int timeInSeconds) {
        AtomicInteger counter = new AtomicInteger(1);
        Awaitility.await().pollInSameThread().atMost(Duration.ofSeconds(timeInSeconds)).pollInterval(Duration.ofSeconds(1)).until(() -> {
            counter.getAndIncrement();
            boolean isTrue = counter.get() == timeInSeconds;
            return isTrue;
        });
    }

    public String generateUserAccessToken(String userId) {
        return generateUserAccessToken("identity_service_url", userId, "apiv2_token");
    }

    public String generateUserAccessToken(String serviceURL, String userId, String tokenName) {
        String baseURL = context.getBean(serviceURL).toString();
        String tokenGenerationEndpoint = "/v1/internal/users/" + userId + "/access-token";
        List<Object> headerValue = new ArrayList<>();
        headerValue.add(context.getBean(tokenName).toString());
        Response getAccessTokenResponse = getMethod(baseURL, tokenGenerationEndpoint, AuthType.XAUTHTOKEN.toString(), 200, headerValue);
        setUserAccessToken(from(getAccessTokenResponse.asString()).get("access_token"));
        return from(getAccessTokenResponse.asString()).get("access_token");
    }

    public boolean isListInAlphabeticalOrder(JSONArray jsonArray, String keyName) {
        boolean isAlphabeticalOrder = true;
        for (int index = 1; index < jsonArray.length(); index++) {
            String currentkeyValue = jsonArray.getJSONObject(index).getString(keyName);
            String previouskeyValue = jsonArray.getJSONObject(index - 1).getString(keyName);
            int minLength = Math.min(currentkeyValue.length(), previouskeyValue.length());
            for (int charindex = 0; charindex < minLength; charindex++) {
                char currentChar = currentkeyValue.charAt(charindex);
                char previousChar = previouskeyValue.charAt(charindex);
                if (currentChar < previousChar) {
                    isAlphabeticalOrder = false;
                    break;
                } else if (currentChar > previousChar) {
                    break;
                }
            }
            if (!isAlphabeticalOrder) {
                break;
            }
        }
        return isAlphabeticalOrder;
    }

    public static void skipIfEnvironment(String targetEnvironment) throws SkipWithReducedStackTrace {
        String currentEnvironment = System.getProperty("env");
        if (targetEnvironment.equals(currentEnvironment)) {
            throw new SkipWithReducedStackTrace(
                    "Skipping this test due to the current environment being: " + currentEnvironment);
        }
    }

    public boolean isRunningOnJenkins() {
        String jenkinsEnv = System.getenv("JENKINS_HOME");
        logger.info("Jenkins home: {}", jenkinsEnv);
        return jenkinsEnv != null;
    }


    public Response postWithFormData(String baseURL, String endpointUrl, int statusCode, String fileName) {
        boolean apiCallStatus = true;
        File filePathName = new File(System.getProperty("user.dir") + "/TestData" + File.separator + fileName);
        if (!filePathName.exists()) {
            throw new IllegalArgumentException("File does not exist:" + filePathName);
        }
        try {
            this.response = (Response) RestAssured.given().baseUri(baseURL).multiPart("name", fileName).multiPart("file", filePathName).when().post(endpointUrl);
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", this.response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }
        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Actual Response Body: " + this.response.getBody().asString()
                    + " Actual Status Code: " + this.response.statusCode()
                    + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public Response postWithFormParams(String baseURL, String endpointUrl, Map<String, Object> parameterMap,
                                       String authType, int statusCode, List<Object> headerValue) {
        boolean apiCallStatus = true;
        HashMap<String, Object> headerMap = headerMap(authType, headerValue);
        try {
            this.response = (Response) RestAssured.given().baseUri(baseURL).headers(headerMap).when().formParams(parameterMap).post(endpointUrl);
            if (this.response.statusCode() == statusCode) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, String.valueOf(statusCode), Integer.toString(this.response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Not null", this.response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, statusCode + " status code is not matching", Integer.toString(this.response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception e) {
            AtuReports.notice("Verify End Point " + endpointUrl, baseURL + endpointUrl, "Error", e.getMessage());
            apiCallStatus = false;
        }
        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point: " + baseURL + endpointUrl, " Actual Response Body: " + this.response.getBody().asString()
                    + " Actual Status Code: " + this.response.statusCode()
                    + " error during execution", "true", Boolean.toString(apiCallStatus));
        }
        return response;
    }

    public Integer getMasterAdminUserId(Integer companyId) {
        String endpoint = "/v1/internal/users?company_id=" + companyId;
        List<Object> headerValue = new ArrayList<>();
        headerValue.add(context.getBean("plat_internal_token").toString());

        return getMethod(context.getBean("APIV2_base_url").toString(), endpoint,
                AuthType.INTERNALAUTHTOKEN.toString(), 200, headerValue).path("users.find { it.role == 'master' }.id");
    }

    public <T> T verifyConditionAndReturnValue(Supplier<T> supplier, int timeout, int pollInterval, Predicate<T> condition) {

        final boolean[] conditionPassed = {false};
        final AtomicReference<T> result = new AtomicReference<>();
        List<String> errorInfo = new ArrayList<>();
        try {
            // Default poll interval is 100ms
            Awaitility.await().atMost(timeout, TimeUnit.SECONDS).pollInterval(pollInterval, TimeUnit.SECONDS).until(() ->
            {
                try {
                    T value = supplier.get();
                    result.set(value);
                    conditionPassed[0] =  condition.test(value);
                    return conditionPassed[0];
                } catch (Error | Exception e) {
                    // Continue polling until the specified time
                    errorInfo.clear();
                    errorInfo.add(e.getMessage());
                    errorInfo.add(Arrays.toString(e.getStackTrace()));
                    return false;
                }
            });
        } catch (Error | Exception e) {
            logger.error("Condition not passed.");
            errorInfo.forEach(logger::error);
            throw new RuntimeException(e);
        }

        return result.get();
    }

    protected List<Object> getHeaders(Integer userId) {
        List<Object> headers = new ArrayList<>();
        headers.add(getAccessToken(userId));
        return headers;
    }

    // AWS environment credentials are required to run locally
    public AwsSessionCredentials connectToAWS() {
        AssumeRoleResponse assumeRoleResponse = null;
        String accessKeyId;
        String secretAccessKey;
        String sessionToken;

        // https://jenkins.knockrentals.com/computer/ - ondemand-cloud instances are running on Knock-Prod (us-west-2 EC2)
        // Use EC2 instance profile to assume knock-qa-test-automation-role in Knock-Alpha and Knock-Beta accounts
        if (isRunningOnJenkins()) {
            logger.info("Test running on Jenkins");

            try (InstanceProfileCredentialsProvider ec2CredentialsProvider =
                         InstanceProfileCredentialsProvider.create()) {
                try (StsClient stsClient = StsClient.builder().region(Region.US_WEST_2)
                        .credentialsProvider(ec2CredentialsProvider).build()) {
                    AssumeRoleRequest assumeRoleRequest = AssumeRoleRequest.builder().roleArn(
                                    getContext().getBean("test_automation_role_arn", String.class))
                            .roleSessionName("KnockTestAutomationSession").build();
                    assumeRoleResponse = stsClient.assumeRole(assumeRoleRequest);
                    accessKeyId = assumeRoleResponse.credentials().accessKeyId();
                    secretAccessKey = assumeRoleResponse.credentials().secretAccessKey();
                    sessionToken = assumeRoleResponse.credentials().sessionToken();
                }
            }
        } else {
            logger.info("Test running on Local");

            // Copy CC_Engineering_ReadWrite role credentials to TestData\awsCred.json file
            JSONObject roleCredentials = new JSONObject(readPayloadFromJson("awsCred.json").replace("\uFEFF", "").trim())
                    .getJSONObject("roleCredentials");
            accessKeyId = roleCredentials.getString("accessKeyId");
            secretAccessKey = roleCredentials.getString("secretAccessKey");
            sessionToken = roleCredentials.getString("sessionToken");
        }

        return new AwsSessionCredentials.Builder()
                .accessKeyId(accessKeyId)
                .secretAccessKey(secretAccessKey)
                .sessionToken(sessionToken)
                .build();
    }

    public JSONObject getRelayPhoneNumber(Integer propertyId) {
        Community community = new Community();
        Map<String, Object> map = community.getSourcesByCommunityId(community.getCommunityId(propertyId), 200).getBody()
                .path("community_sources_data.find{it.relay_phones?.number != null}");
        return new JSONObject(map);
    }

    public JSONObject retrieveTwilioCreds() {
        SecretsManagerClientBuilder secretsManagerClientBuilder = SecretsManagerClient.builder()
                .region(Region.US_EAST_1).credentialsProvider(StaticCredentialsProvider.create(connectToAWS()));

        GetSecretValueResponse secretValueResponse;
        try (SecretsManagerClient client = secretsManagerClientBuilder.build()) {
            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                    .secretId("knock/testAutomation/twilioCred").build();
            secretValueResponse = client.getSecretValue(getSecretValueRequest);
        }
        AssertionUtils.assertTrue("Verify response status code",
                secretValueResponse.sdkHttpResponse().statusCode() == 200);

        return new JSONObject(secretValueResponse.secretString());
    }

    public JsonNode convertToJson(String strToConvert) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(strToConvert);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonNode;
    }
}