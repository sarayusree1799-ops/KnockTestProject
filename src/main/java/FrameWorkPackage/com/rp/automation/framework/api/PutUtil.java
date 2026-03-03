package FrameWorkPackage.com.rp.automation.framework.api;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import io.cucumber.java.bs.A;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
import java.util.Map;

public class PutUtil {
    public static Map<String, Object> getRespBodyOfPutMethod(String baseUri, String testUri, String access_token,
                                                             String body, int expectedStatusCode, String msg) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean result = true;
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
            response = httpRequest.put(testUri);

            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg,
                        "URI:" + "\n" + testUri + "  ------>\n  " + "BODY:" + "\n" + body,
                        "JSON content : Not null", response.asString());
            } else {
                AtuReports.failResults(msg,
                        "URI:" + "\n" + testUri + "  ------>\n  " + "BODY:" + "\n" + body,
                        expectedStatusCode + " status Code is Not Matching",
                        response.statusCode() + "" + response.asString());
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

    public static Map<String, Object> getRespBodyOfPutMethodwithSchemaValidation(String baseUri, String testUri,
                                                                                 String access_token, String body, int expectedStatusCode, String expectedSchema, String msg) {
        Map<String, Object> resp = new HashMap<String, Object>();
        String apiCallResp = null;
        boolean apiCallStatus = true;
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
            response = httpRequest.put(testUri);

            if (response.statusCode() == expectedStatusCode) {
                apiCallStatus = true;
                AtuReports.passResults(msg,
                        "URI:" + "\n" + testUri + " ------>\n " + "BODY:" + "\n" + body,
                        "JSON content : Not null", response.asString());

                String respJson = response.body().asString();
                ProcessingReport result = JsonSchemaValidationUtil.getResult(expectedSchema, respJson);
                schemaCallStatus = result.isSuccess();
                apiCallResp = response.asString();
                ApiCommonUtils.isTrue(result.isSuccess(), testUri, respJson, result.toString());
            } else {
                apiCallStatus = false;
                AtuReports.failResults(msg, "URI:" + "\n" + testUri + " -------->\n " + "BODY:" + "\n" + body,
                        "JSON Content : NOT NULL", response.asString());
            }
            apiCallResp = response.asString();
        } catch (Exception em) {
            schemaCallStatus = false;
            AtuReports.notice(" API Response is Error details", testUri, "Error", em.getMessage());
        }
        resp.put("STATUSCODE", apiCallStatus);
        resp.put("SCHEMA", schemaCallStatus);
        resp.put("JSON", apiCallResp);
        return resp;
    }

    public static boolean verifyPut(String baseUri, String testUri, String access_token, String body,
                                    int expectedStatusCode, String msg) {
        boolean apiCallStatus = true;
        try {
            RestAssured.defaultParser = Parser.JSON;
            RestAssured.baseURI = baseUri;
            RequestSpecification httpRequest = RestAssured.given();
            httpRequest.header("Authorization", "Bearer " + access_token);
            if (body != "") {
                httpRequest.body(body);
            }
            httpRequest.contentType(ContentType.JSON);
            Response response = httpRequest.put(testUri);

            if (response.statusCode() == expectedStatusCode) {
                AtuReports.passResults(msg,
                        "URI:" + "\n" + testUri + " ------>\n " + "BODY:" + "\n" + body,
                        "Status Code:" + expectedStatusCode,
                        response.statusCode() + "");
            } else {
                AtuReports.failResults(msg,
                        "URI:" + "\n" + testUri + " ------>\n " + "BODY:" + "\n" + body,
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
}
