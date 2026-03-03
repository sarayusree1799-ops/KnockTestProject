package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.annotations.AzureSuiteIDTestCaseId;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import FrameWorkPackage.com.rp.automation.framework.annotations.AzureTestCaseId;
import FrameWorkPackage.com.rp.automation.framework.annotations.AzureSuiteIDTestCaseId;
import org.apache.http.util.EntityUtils;
import org.testng.ITestResult;


public class AzureUtils {
    public HttpClient httpClient = HttpClientBuilder.create().build();
    public String organizationUrl = "https://tfs.realpage.com/tfs/Realpage/";
    public String pAToken = "q3gyfsopv5y573on23u2otkqnxxuivykwmpxtqrvda2d7koqca"; // svc-automation
    public String AuthStr = ":" + pAToken;
    public Base64 base64 = new Base64();
    public String encodedPAT = new String(base64.encode(AuthStr.getBytes()));


    public void updateTestCaseExecution(String projectName, String testCaseId, String executionStatus)
            throws ClientProtocolException, IOException {

        if (!planId.contains("NoParameters") && !suiteId.contains("NoParameters")) {

            String[] azureIds = testCaseId.split(",");
            if (azureIds.length > 1) {
                for (String azuTestCaseId : azureIds) {

                    //STEP-1: GET list of all points for a given suite:
                    //GET https://tfs.realpage.com/tfs/Realpage/Enterprise_Initiatives/_apis/test/plans/503308/suites/503309/points?testCaseId=503311&api-version=5.0
                    getTestCasePoints(projectName, planId, suiteId, azuTestCaseId, executionStatus);

                }
            } else {
                getTestCasePoints(projectName, planId, suiteId, testCaseId, executionStatus);
            }

        }
    }

    public void updateTestCaseExecution(ITestResult results) throws ClientProtocolException, IOException {
        try {
            if (results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class) != null) {

                String projectName = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureTestCaseId.class).productName();
                String[] azureIds = results.getMethod().getConstructorOrMethod().getMethod()
                        .getAnnotation(AzureTestCaseId.class).testCaseIds();

                System.out.println("projectName " + projectName);
                System.out.println("azureIds " + azureIds[0]);
                System.out.println("suiteId " + suiteId);

                if (projectName != null && azureIds != null) {
                    if (!planId.contains("NoParameters") && !suiteId.contains("NoParameters")) {
                        System.out.println("azureIds.length " + azureIds.length);
                        // String[] azureIds = azureIds.split(",");
                        if (azureIds.length > 1) {
                            for (String azuTestCaseId : azureIds) {
                                // STEP-1: GET list of all points for a given suite:
                                // GET
                                //
                                // https://tfs.realpage.com/tfs/Realpage/Enterprise_Initiatives/_apis/test/Plans/503308/Suites/503309/points?testCaseId=503311&api-version=5.0
                                getTestCasePoints(projectName, planId, suiteId, azuTestCaseId, String.valueOf(results.getStatus()));
                            }
                        } else {
                            getTestCasePoints(projectName, planId, suiteId, azureIds[0],
                                    String.valueOf(results.getStatus()));
                        }
                    }
                }
            }
            if (results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class) != null
                    && results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class).suiteId() != null) {
                suiteId = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class).suiteId();

                String projectName = results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(AzureSuiteIDTestCaseId.class).productName();

                String[] azureIds = results.getMethod().getConstructorOrMethod().getMethod()
                        .getAnnotation(AzureSuiteIDTestCaseId.class).testCaseIds();
                if (projectName != null && azureIds != null) {
                    if (!planId.contains("NoParameters") && !suiteId.contains("NoParameters")) {
                        System.out.println("azureIds.length " + azureIds.length);
                        // String[] azureIds = azureIds.split(",");
                        if (azureIds.length > 1) {
                            for (String azuTestCaseId : azureIds) {
                                // STEP-1: GET list of all points for a given suite:
                                // GET
                                //
                                // https://tfs.realpage.com/tfs/Realpage/Enterprise_Initiatives/_apis/test/Plans/503308/Suites/503309/points?testCaseId=503311&api-version=5.0

                                getTestCasePoints(projectName, planId, suiteId, azuTestCaseId, String.valueOf(results.getStatus()));
                            }
                        } else {
                            getTestCasePoints(projectName, planId, suiteId, azureIds[0], String.valueOf(results.getStatus()));
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param testCaseId
     * @param executionStatus
     * @thows IOException
     * @thows ClientProtocolException
     * thows JsonProcessingException
     */
    private void getTestCasePoints(String projectName, String planId, String suiteId, String testCaseId, String executionStatus)
            throws ClientProtocolException, IOException, JsonProcessingException {
        HttpGet getRequest = new HttpGet(
                organizationUrl + "/_apis/test/Plans/" + planId
                        + "/Suites/" + suiteId
                        + "/points?testCaseId=" + testCaseId
                        + "&api-version=5.0");
        HttpResponse response = httpClient.execute(getRequest);
        String json_string = EntityUtils.toString(response.getEntity());
        //System.out.println("out is" + json_string);

        ObjectMapper objectMapper = new ObjectMapper();
        com.fasterxml.jackson.databind.JsonNode jsonNodes =
                (com.fasterxml.jackson.databind.JsonNode) objectMapper.readTree(json_string);

        com.fasterxml.jackson.databind.JsonNode valueJsonNodes = jsonNodes.get("value");
        //System.out.println("valueJsonNodes : " + valueJsonNodes.size());

        if (executionStatus.toString().equalsIgnoreCase("1")) {
            executionStatus = "Passed";
        } else if (executionStatus.toString().equalsIgnoreCase("2")) {
            executionStatus = "Failed";
        }

        System.out.println("executionStatus :" + executionStatus);

        for (com.fasterxml.jackson.databind.JsonNode jsonNode : valueJsonNodes) {

            // if (jsonNode.get("configuration").toString().contains("Windows")) {

            updateTestPoint(projectName, planId, suiteId,
                    jsonNode.get("id").toString(), executionStatus);
            // }
        }
    }

    public void updateTestPoint(String projectName, String planId, String suiteId, String points, String outcome) throws ClientProtocolException, IOException {
        // STEP-2: From the list, find the pointID for given TestCase.ID and update all testpoints of this testcase for outcome = Passed // TODO: In future, isolate update for different configurations (like WINDOWS, UNIX, etc.)
        // PATCH https://tfs.realpage.com/tfs/Realpage/Enterprise_Initiatives/_apis/test/Plans/503308/Suites/503309/points/525353?api-version=5.0
        HttpPatch patchRequest = new HttpPatch(organizationUrl + projectName + "/_apis/test/Plans/" + planId + "/Suites/" + suiteId + "/points/" + points + "?api-version=5.0");
        String reqbody = "{\"outcome\": \"" + outcome + "\" }";
        // System.out.println("reqbody " + reqbody);
        HttpResponse response1 = httpClient.execute(setRequestHeader(patchRequest, reqbody));
        String json_string1 = EntityUtils.toString(response1.getEntity());
        // System.out.println("patchRequest Out is" + json_string1);
    }

    /**
     * @param requestHeader
     */
    private HttpPatch setRequestHeader(HttpPatch requestHeader, String reqbody) {

        requestHeader.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        requestHeader.setHeader(HttpHeaders.ACCEPT, "application/json");
        requestHeader.setHeader("Authorization", "Basic " + encodedPAT);
        requestHeader.setEntity(
                new StringEntity(reqbody, ContentType.APPLICATION_JSON));

        return requestHeader;
    }

    /**
     * @param requestHeader
     */
    private HttpGet setRequestHeader(HttpGet requestHeader) {

        requestHeader.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        requestHeader.setHeader(HttpHeaders.ACCEPT, "application/json");
        requestHeader.setHeader("Authorization", "Basic " + encodedPAT);
        return requestHeader;
    }
}
