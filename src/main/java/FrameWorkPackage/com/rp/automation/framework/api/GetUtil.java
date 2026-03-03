package FrameWorkPackage.com.rp.automation.framework.api;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.google.gson.JsonParser;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class GetUtil {
    /**
     * getRespBodyOfGetMethod using basic Authentication
     * @param baseUri
     * @param testUri
     * @param userName
     * @param pwd
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfGetMethod(String baseUri, String testUri, String userName,
                                                             String pwd, String body, int expectedStatusCode, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
        Response response = null;
        try {
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = given();
            httpRequest.auth().preemptive().basic(userName, pwd);
            if (body != "")
                httpRequest.body(body);
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.get(testUri);
            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg, "URI: " + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        "JSON content : ", response.asString());
            } else {
                AtuReports.failResults(msg, "URI: " + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        expectedStatusCode + " Code is Not Matching", response.statusCode() + "" + response.asString());
                result = false;
            }
        } catch (Exception em) {
            AtuReports.failResults("API Response is error details: ", testUri, "Error", em.getMessage());
            result = false;
        }
        String str = response.asString();
        map.put("Result", result);
        map.put("Response", str);
        return map;
    }

    /**
     * getRespBodyOfGetMethod using access token
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfGetMethod(String baseUri, String testUri, String access_token,
                                                             String body, int expectedStatusCode, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
        Response response = null;
        try {
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "")
                httpRequest.body(body);
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.get(testUri);
            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg, "URI: " + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        "JSON content : Not null", response.asString());
            } else {
                AtuReports.failResults(msg, "URI: " + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        expectedStatusCode + " status Code is Not Matching",
                        response.statusCode() + "" + response.asString());
                result = false;
            }
        } catch (Exception em) {
            AtuReports.failResults("API Response is Error details", testUri, "Error", em.getMessage());
            result = false;
        }
        String str = response.asString();
        map.put("Result", result);
        map.put("Response", str);
        return map;
    }

    /**
     * getRespBodyOfGetMethodWithSchemaValidation
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param expectedStatusCode
     * @param expectedSchema
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfGetMethodWithSchemaValidation(String baseUri, String testUri,
                                                                                 String access_token, String body, int expectedStatusCode, String expectedSchema, String msg) {
        Map<String, Object> resp = new HashMap<String, Object>();
        String apiCallResp = null;
        boolean apiCallStatus = true;
        boolean schemaCallStatus = true;
        try {
            Response response = null;
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "")
                httpRequest.body(body);
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.get(testUri);
            if (response.statusCode() == expectedStatusCode) {
                apiCallStatus = true;
                AtuReports.passResults(msg, "URI: " + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        "JSON content : Not null", response.asString());
                String respJson = response.body().asString();
                System.out.println(respJson);
                ProcessingReport result = JsonSchemaValidationUtil.getResult(expectedSchema, respJson);
                schemaCallStatus = result.isSuccess();
                ApiCommonUtils.isTrue(result.isSuccess(), testUri, respJson, result.toString());
            } else {
                apiCallStatus = false;
                AtuReports.failResults(msg, "URI: " + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        expectedStatusCode + " Status Code is Not Matching",
                        response.statusCode() + " " + response.asString());
            }
            apiCallResp = response.asString();
        } catch (Exception em) {
            apiCallStatus = false;
            AtuReports.notice(em.getMessage(), testUri, "Error", em.getMessage());
        }
        resp.put("STATUSCODE", apiCallStatus);
        resp.put("SCHEMA", schemaCallStatus);
        resp.put("STATUSCODE", apiCallResp);
        return resp;
    }

    /**
     * getRespBodyOfGetMethodWithSchemaValidation
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static boolean verifyGet(String baseUri, String testUri, String access_token, String body,
                                    int expectedStatusCode, String msg) {
        boolean apiCallStatus = true;
        try {
            Response response = null;
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "")
                httpRequest.body(body);
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.get(testUri);
            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg, testUri, expectedStatusCode + "", Integer.toString(response.statusCode()));
            } else {
                AtuReports.failResults(msg, testUri, expectedStatusCode + "", Integer.toString(response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Exception em) {
            AtuReports.notice(" API Response is", testUri, "Error", em.getMessage());
            apiCallStatus = false;
        }
        return apiCallStatus;
    }

    /**
     * With Cookie
     * @param cookievalue
     * @param endpointUrl
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfGetMethod(String cookievalue, String endpointUrl,
                                                             int expectedStatusCode, String msg) {

        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
        Response response = null;

        try {
            response = given().header("Cookie", cookievalue).contentType("application/json").when().get(endpointUrl);

            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg, "URI:" + "\n" + endpointUrl + "  ------>\n ", "JSON content : Not null",
                        response.asString());
            } else {
                AtuReports.failResults(msg, "URI:" + "\n" + endpointUrl + "  ------>\n ",
                        expectedStatusCode + " status Code is Not Matching",
                        response.statusCode() + "" + response.asString());
                result = false;
            }
        } catch (Exception em) {
            AtuReports.failResults(" API Response is Error details", endpointUrl, "Error", em.getMessage());
            result = false;
        }

        String str = response.asString();
        map.put("Result", result);
        map.put("Response", str);
        return map;
    }

    /**
     * GetRespBodyOfGetMethodForDownload and Cookie
     * @param cookievalue
     * @param endpointUrl
     * @param expectedStatusCode
     * @param msg
     * @return
     */

    public static Map<String, Object> getRespBodyOfGetMethodForDownload(String cookievalue, String endpointUrl,
                                                                        int expectedStatusCode, String msg) {

        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
        Response response = null;

        try {
            response = given().header("Cookie", cookievalue).contentType("application/json").when().get(endpointUrl);

            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg, "URI:" + "\n" + endpointUrl + " ------>\n ", "JSON content : Not null",
                        response.asString());
            } else {
                AtuReports.failResults(msg, "URI:" + "\n" + endpointUrl + " ------>\n ",
                        expectedStatusCode + " status Code is Not Matching",
                        response.statusCode() + "" + response.asString());
                result = false;
            }
        } catch (Exception em) {
            AtuReports.failResults(" API Response is Error details", endpointUrl, "Error", em.getMessage());
            result = false;
        }

        byte[] str = response.asByteArray();
        map.put("Result", result);
        map.put("Response", str);
        return map;
    }

    //Unused
    public static String getKeyValueFromResponseBodyOfGetMethod__delete(String testUri, String access_token, String body,
                                                                        int expectedStatusCode, String key, String msg) {
        String apiCallResp = null;
        try {
            Response response = given().header("Authorization", "Bearer " + access_token)
                    .contentType("application/json").body(body).when().get(testUri);
            if (expectedStatusCode == 200) {
                if (!key.isEmpty()) {
                    if (new JsonParser().parse(response.asString()).getAsJsonArray().get(1).getAsJsonObject() != null) {
                        apiCallResp = new JsonParser().parse(response.asString()).getAsJsonArray().get(1)
                                .getAsJsonObject().get(key).toString();
                    } else {
                        apiCallResp = null;
                    }
                } else {
                    apiCallResp = response.asString();
                }
            } else {
                AtuReports.failResults(msg, testUri, expectedStatusCode + "", Integer.toString(response.statusCode()));
            }
        } catch (Exception em) {
            AtuReports.notice("API Response is Error details", testUri, "Error", em.getMessage());
        }
        return apiCallResp;
    }
}