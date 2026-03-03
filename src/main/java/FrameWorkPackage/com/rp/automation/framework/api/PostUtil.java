package FrameWorkPackage.com.rp.automation.framework.api;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class PostUtil {
    /**
     * getRespBodyOfPostMethod using Access Token
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfPostMethod(String baseUri, String testUri, String access_token,
                                                              String body, int expectedStatusCode, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
        Response response = null;
        try {
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "")
                httpRequest.body(body);
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.post(testUri);
            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg, "URI:" + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        "JSON content : null", response.asString());
                System.out.println(response.asString());
            } else {
                AtuReports.failResults(msg, "URI:" + "\n" + testUri + " ------->\n " + "BODY:" + "\n" + body,
                        expectedStatusCode + " status Code is Not Matching",
                        response.statusCode() + " " + response.asString());
                result = false;
            }
        } catch (Exception em) {
            AtuReports.failResults(" API Response is Error details", testUri, "Error", em.getMessage());
            result = false;
        }
        String str = response.asString();
        map.put("Result", result);
        map.put("Response", str);
        return map;
    }

    /**
     * getRespBodyOfPostMethodWithSchemaValidation
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param expectedStatusCode
     * @param expectedSchema
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfPostMethodWithSchemaValidation(String baseUri, String testUri,
                                                                                  String access_token, String body, int expectedStatusCode, String expectedSchema, String msg) {
        Map<String, Object> resp = new HashMap<String, Object>();
        String apiCallResp = null;
        boolean apiCallStatus = false;
        boolean schemaCallStatus = true;
        Response response = null;
        try {
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "") {
                httpRequest.body(body);
            }
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.post(testUri);
            if (response.statusCode() == expectedStatusCode) {
                apiCallStatus = true;
                AtuReports.passResults(msg, "URI:" + "\n" + testUri + " -------\n " + "BODY:" + "\n" + body,
                        "JSON content : Not null", response.asString());
                String respJson = response.body().asString();
                System.out.println(response.body().asString());
                apiCallResp = response.asString();
                ProcessingReport result = JsonSchemaValidationUtil.getResult(expectedSchema, respJson);
                schemaCallStatus = result.isSuccess();
                ApiCommonUtils.isTrue(result.isSuccess(), testUri, respJson, result.toString());
            } else {
                apiCallStatus = false;
                AtuReports.failResults(msg, "URI:" + "\n" + testUri + " -------\n " + "BODY:" + "\n" + body,
                        expectedStatusCode + " Status Code is Not Matching",
                        response.statusCode() + " " + response.asString());
            }
        } catch (Exception em) {
            AtuReports.notice(" API Response is Error details", testUri, "Error", em.getMessage());
            schemaCallStatus = false;
        }
        resp.put("STATUSCODE", apiCallStatus);
        resp.put("SCHEMA", schemaCallStatus);
        resp.put("JSON", apiCallResp);
        return resp;
    }

    /**
     * verifyBasicAuthPostCall
     * @param baseUri
     * @param testUri
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static boolean verifyBasicAuthPostCall(String baseUri, String testUri, String body, int expectedStatusCode,
                                                  String msg) {
        boolean apiCallStatus = true;
        try {
            Response response = null;
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.auth().basic("autotest", "autotest")
                    .contentType("application/json;charset=UTF-8");
            if (body != "") {
                httpRequest.body(body);
            }
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.post(testUri);
            if (response.statusCode() == 200) {
                AtuReports.passResults(msg, testUri, "200",
                        Integer.toString(response.statusCode()));
            } else {
                AtuReports.failResults(msg, testUri, "200",
                        Integer.toString(response.statusCode()));
                apiCallStatus = false;
            }

        } catch (Exception em) {
            AtuReports.notice(msg, testUri, "Error", em.getMessage());
            apiCallStatus = false;
        }
        return apiCallStatus;
    }

    /**
     * verifyPost-Verify Status Code
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static boolean verifyPost(String baseUri, String testUri, String access_token, String body,
                                     int expectedStatusCode, String msg) {
        boolean apiCallStatus = true;
        try {
            Response response = null;
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "") {
                httpRequest.body(body);
            }
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.post(testUri);
            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg,
                        "URI:" + "\n" + testUri + "  ------>\n  " + "BODY:" + "\n" + body,
                        "Status Code:" + expectedStatusCode,
                        response.statusCode() + "");
                System.out.println(response.asString());
            } else {
                AtuReports.failResults(msg,
                        "URI:" + "\n" + testUri + "  ------>\n  " + "BODY:" + "\n" + body,
                        expectedStatusCode + " status Code is Not Matching",
                        response.statusCode() + "" + response.asString());
                apiCallStatus = false;
            }
        } catch (Exception em) {
            AtuReports.notice(msg, testUri, "Error", em.getMessage());
            apiCallStatus = false;
        }
        return apiCallStatus;
    }

    /**
     * verifyPostAccept
     * @param baseUri
     * @param testUri
     * @param access_token
     * @param body
     * @param acceptParam
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static boolean verifyPostAccept(String baseUri, String testUri, String access_token, String body,
                                           String acceptParam, int expectedStatusCode, String msg) {
        boolean apiCallStatus = true;
        try {
            Response response = null;
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.header("Authorization", "Bearer " + access_token).accept(acceptParam);
            if (body != "") {
                httpRequest.body(body);
            }
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.post(testUri);

            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg,
                        "URI:" + "\n" + testUri + "  ------>\n  " + "BODY:" + "\n" + body,
                        "JSON Content : Not null", response.asString());
            } else {
                AtuReports.failResults(msg,
                        "URI:" + "\n" + testUri + "  ------>\n  " + "BODY:" + "\n" + body,
                        expectedStatusCode + " status code is NOT matching",
                        response.statusCode() + "" + response.asString());
                apiCallStatus = false;
            }
        } catch (Throwable em) {
            AtuReports.notice(msg, testUri, "Error", em.getMessage());
            apiCallStatus = false;
        }
        return apiCallStatus;
    }

    /**
     * with cookie, POST api call
     * @param cookieValue
     * @param endpointUrl
     * @param body
     * @param expectedStatusCode
     * @param msg
     * @return
     */
    public static Map<String, Object> getRespBodyOfPostMethod(String cookieValue, String endpointUrl, String body,
                                           int expectedStatusCode, String msg) {
        Map<String, Object> map = new HashMap<>();
        boolean result = true;
        Response response = null;
        try {
            RestAssured.defaultParser = Parser.JSON;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.header("Cookie", cookieValue);
            if (body != "") {
                httpRequest.body(body);
            }
            httpRequest.contentType(ContentType.JSON);
            response = httpRequest.post(endpointUrl);

            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg,
                        "URI:" + "\n" + endpointUrl + "  ------>\n  ",
                        "JSON Content : Not null", response.asString());
            } else {
                AtuReports.failResults(msg,
                        "URI:" + "\n" + endpointUrl + "  ------>\n  ",
                        expectedStatusCode + " status code is NOT matching",
                        response.statusCode() + "" + response.asString());
                result = false;
            }
        } catch (Exception em) {
            AtuReports.failResults("API Response is error details", endpointUrl, "Error", em.getMessage());
            result = false;
        }
        String str = response.body().asString();
        map.put("Result", result);
        map.put("Response", str);
        return map;
    }
}