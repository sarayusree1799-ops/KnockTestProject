package FrameWorkPackage.com.rp.automation.framework.api;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter;
import FrameWorkPackage.com.rp.automation.framework.util.Reporter.TestStatus;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.apache.xmlbeans.impl.soap.*;
import org.json.XML;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import com.jayway.jsonpath.Configuration;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.given;

public class BasicApiUtil {
    private Response response = null;

    public String getAuthToken(String baseUrl, String endpointUrl, String uname, String pwd) {
        Response response = null;
        String access_token = null;

        // Concatenate username and password for authentication
        String authString = uname + ":" + pwd;
        byte[] base64Encoded = Base64.getEncoder().encode(authString.getBytes());
        String encodedString = new String(base64Encoded);

        // Send request to obtain the authentication token
        response = given().header("Authorization", "Basic " + encodedString).when().get(baseUrl + endpointUrl);

        if (response.statusCode() == 200) {
            AtuReports.passResults1("Token Generated Status" + endpointUrl, baseUrl + endpointUrl, "200",
                    Integer.toString(response.statusCode()));
            // Extract the JSON response
            ExtractableResponse<Response> jsonResp = response.then().contentType(ContentType.JSON).extract();

            // Retrieve and return the access token from the JSON response
            access_token = jsonResp.path("access_token");
        } else {
            AtuReports.notice("Token Generated Status" + endpointUrl, baseUrl + endpointUrl,
                    "200 status Code is Not Matching", Integer.toString(response.statusCode()));
        }

        return access_token;
    }

    /**
     * Retrieves the authentication token using the provided credentials.
     * @param uname The username for authentication.
     * @param pwd The password for authentication.
     * @return The access token obtained from the authentication response.
     */
    public String getAuthToken(String uname, String pwd) {
        Response response = null;

        // Concatenate username and password for authentication
        String authString = uname + ":" + pwd;
        byte[] base64Encoded = Base64.getEncoder().encode(authString.getBytes());
        String encodedString = new String(base64Encoded);

        // Send request to obtain the authentication token
        response = given().header("Authorization", "Basic " + encodedString).when()
                .get(WebDriverBase.context.getBean("envURL") + "auth/token");

        // Extract the JSON response
        ExtractableResponse<Response> jsonResp = response.then().contentType(ContentType.JSON).extract();

        // Retrieve the access token from the JSON response
        String access_token = jsonResp.path("access_token");

        // Check if the access token is null and report failure if it is
        if (access_token == null)
            AtuReports.failResults("Auth Token", "should not be null", "Not Null", access_token);

        return access_token;
    }

    /*
    * Method to get Auth token without auth/token
    * @return
    */

    public String getAuth(String uname, String pwd) {
        String authString = uname + ":" + pwd;
        byte[] base64Encoded = Base64.getEncoder().encode(authString.getBytes());
        String encodedString = new String(base64Encoded);
        return encodedString;
    }

    /**
     Retrieves the authentication token from the specified base URL using the provided credentials.
     @param baseUrl The base URL of the authentication endpoint.
     @param uname The username for authentication.
     @param pwd The password for authentication.
     @return The access token obtained from the authentication response.
     */
    public String getAuthToken(String baseUrl, String uname, String pwd) {
        Response response = null;

        // Concatenate username and password for authentication
        String authString = uname + ":" + pwd;
        byte[] base64Encoded = Base64.getEncoder().encode(authString.getBytes());
        String encodedString = new String(base64Encoded);

        // Send request to obtain the authentication token
        response = given().header("Authorization", "Basic " + encodedString).when().get(baseUrl + "auth/token");

        // Extract the JSON response
        ExtractableResponse<Response> jsonResp = response.then().contentType(ContentType.JSON).extract();

        // Retrieve and return the access token from the JSON response
        String access_token = jsonResp.path("access_token");
        return access_token;
    }

    /**
     * Performs a GET request to the specified endpoint URL.
     * @param endpointUrl The URL of the endpoint to call.
     */
    public void getCall(String endpointUrl) {
        boolean apiCallStatus = true;
        try {
            response = given().contentType("application/json").when().get(endpointUrl);

            if (response.statusCode() == 200) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl, "200",
                        Integer.toString(response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl,
                        "Not null", response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl,
                        "200 status Code is Not Matching", Integer.toString(response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Throwable em) {
            AtuReports.notice("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl, "Error",
                    em.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point " + endpointUrl,
                    RestAssured.baseURI + endpointUrl + " Error during execution", "true",
                    Boolean.toString(apiCallStatus));
        }
    }

    /**
     * Performs a POST request to the specified endpoint URL.
     * @param userName The username for authentication.
     * @param password The password for authentication.
     * @param bodyString The request body as a string.
     * @param endpointUrl The URL of the endpoint to call.
     * @return The response object received from the POST request.
     */
    public Response postCall(String userName, String password, String bodyString, String endpointUrl) {
        boolean apiCallStatus = true;

        try {
            response = given().auth().preemptive().basic(userName, password).contentType("application/json")
                    .body(bodyString).when().post(endpointUrl);

            if (response.statusCode() == 200) {
                AtuReports.passResults1("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl, "200",
                        Integer.toString(response.statusCode()));
                AtuReports.passResults1("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl,
                        "Not null", response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl,
                        "200 status Code is Not Matching", Integer.toString(response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Throwable em) {
            AtuReports.notice("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl, "Error",
                    em.getMessage());
            apiCallStatus = false;
        }
        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point" + endpointUrl, RestAssured.baseURI + endpointUrl + " error during execution",
                    "true", Boolean.toString(apiCallStatus) );
        }
        return response;
    }

    /**
     * Performs a PUT request to the specified endpoint URL.
     * @param bodyString The request body as a string.
     * @param endpointUrl The URL of the endpoint to call.
     */
    public void putCall(String bodyString, String endpointUrl) {
        boolean apiCallStatus = true;
        try {
            response = given().contentType("application/json").body(bodyString).when().put(endpointUrl);

            if (response.statusCode() == 200) {
                AtuReports.passResults("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl, "200",
                        Integer.toString(response.statusCode()));
                AtuReports.passResults("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl,
                        "Not null", response.getBody().asString());
            } else {
                AtuReports.notice("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl,
                        "200 status Code is Not Matching", Integer.toString(response.statusCode()));
                apiCallStatus = false;
            }
        } catch (Throwable em) {
            AtuReports.notice("Verify End Point " + endpointUrl, RestAssured.baseURI + endpointUrl, "Error",
                    em.getMessage());
            apiCallStatus = false;
        }

        if (!apiCallStatus) {
            AtuReports.failResults("Verify End Point " + endpointUrl,
                    RestAssured.baseURI + endpointUrl + " error during execution", "true",
                    Boolean.toString(apiCallStatus));
        }
    }

    /**
     Retrieves a list of database names from a given list of property names.
     @param propertyNameList The list of property names.
     @return The list of database names extracted from the property names.
     */
    public List<String> getDBList(List<Map<String, Object>> propertyNameList) {
        List<String> dbList = new ArrayList<String>();
        for (int i = 0; i < propertyNameList.size(); i++) {
            Map<String, Object> map = propertyNameList.get(i);
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                dbList.add((String) entry.getValue());
            }
        }
        return dbList;
    }

    /**
     Retrieves the current month in the format "Y{year}M{month}".
     @param date The reference date.
     @return The aggregation level representing the current month.
     */
    public String getCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date1 = (java.util.Date) calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        DateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(date);
        String aggregationLevelCurrent = "Y" + year + "M" + dateFormat.format(date1);
        return aggregationLevelCurrent;
    }

    /**
     Retrieves the aggregation level for the last month in the format "Y{year}M{month}".
     @return The aggregation level representing the last month.
     */
    public String getLastMonth() {
        DateFormat df = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
        Calendar calendar = Calendar.getInstance();
        java.util.Date date1 = (java.util.Date) calendar.getTime();
        String year = df.format(date1);
        calendar.add(Calendar.MONTH, -1);
        String aggregationLevelCurrent = "Y" + year + "M" + dateFormat.format(calendar.getTime());
        return aggregationLevelCurrent;
    }

    /**
     Retrieves the aggregation level for the current quarter in the format "Y{year}Q{quarter}".
     @param date The reference date.
     @return The aggregation level representing the current quarter.
     */
    public String getQuarter(Date date) {
        @SuppressWarnings("deprecation")
        int currentQuarter = (date.getMonth() / 3) + 1;

        DateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(date);
        String aggregationLevelCurrent = "Y" + year + "Q" + currentQuarter;

        return aggregationLevelCurrent;
    }

    /**
     Retrieves the aggregation level for the current quarter in the format "Y{year}Q{quarter}".
     @param date The reference date.
     @return The aggregation level representing the current quarter.
     */
    public String getCurrentQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date1 = (java.util.Date) calendar.getTime();
        @SuppressWarnings("deprecation")
        int currentQuarter = (date1.getMonth() / 3) + 1;
        DateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(date);
        String aggregationLevelCurrent = "Y" + year + "Q" + currentQuarter;
        return aggregationLevelCurrent;
    }

    /**
     Retrieves the aggregation level for the last quarter in the format "Y{year}Q{quarter}".
     @param date The reference date.
     @return The aggregation level representing the last quarter.
     */
    public String getLastQuarter(Date date) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date1 = (java.util.Date) calendar.getTime();
        @SuppressWarnings("deprecation")
        int lastQuarter = (date1.getMonth() / 3);
        DateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(date);
        String aggregationLevelLast = "Y" + year + "Q" + lastQuarter;
        return aggregationLevelLast;
    }

    /**
     Retrieves the current year in the format "Y{year}M{month}".
     @param date The reference date.
     @return The aggregation level representing the current year.
     */
    public String getCurrentYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date1 = (java.util.Date) calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");

        DateFormat df = new SimpleDateFormat("yyyy");
        String year = df.format(date);
        String aggregationLevelCurrent = "Y" + year + "M" + dateFormat.format(date1);

        return aggregationLevelCurrent;
    }

    /**
     Retrieves the aggregation level for the last year in the format "Y{year}M{month}".
     @return The aggregation level representing the last year.
     */
    public String getLastYear() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date1 = (java.util.Date) calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM");

        String aggregationLevelCurrent = "Y" + getPreviousYear() + "M" + dateFormat.format(date1);

        return aggregationLevelCurrent;
    }

    /**
     * Retrieves the previous year.
     * @return The previous year as an integer.
     */
    private static int getPreviousYear() {
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);
        return prevYear.get(Calendar.YEAR);
    }

    public SOAPMessage createSOAPRequest(String serverURI, String nameSpace, String funtion, String client,
                                         String propId) throws Exception {
        // Next, create the actual message
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(nameSpace, serverURI);

        // start: setting HTTP headers - optional, comment out if not needed
        String authorization = Base64Coder.encodeString("aeonesitetest:pkiFN7HU"); // SAT=
        // aesat:eTheith7
        MimeHeaders hd = soapMessage.getMimeHeaders();
        hd.addHeader("Authorization", "Basic " + authorization);
        hd.addHeader("SOAPAction", "\"\"");
        // Create and populate the body
        SOAPBody soapBody = envelope.getBody();

// Create the main element and namespace
        SOAPElement soapBodyElem = soapBody.addChildElement(funtion, nameSpace);
        SOAPElement soapBodyreq = soapBodyElem.addChildElement("request", nameSpace);
        soapBodyreq.addChildElement("clientName", nameSpace).addTextNode(client);
        soapBodyreq.addChildElement("externalPropertyId", nameSpace).addTextNode(propId);

// Save the message
        soapMessage.saveChanges();

// Check the input
// System.out.println("Request SOAP Message = ");
// soapMessage.writeTo(System.out);
        return soapMessage;
    }

    /**
     * Method used to print the SOAP Response
     *
     * @return
     */
    public Object getSoapJson(SOAPMessage soapResponse) throws Exception {

        // Create the transformer
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Gson gson = new Gson();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        final ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
        final StreamResult result = new StreamResult(streamOut);
        transformer.transform(sourceContent, result);
        org.json.JSONObject soap = XML.toJSONObject(streamOut.toString());
        String soapJson = gson.toJson(soap);
        Object soapDocument = Configuration.defaultConfiguration().jsonProvider().parse(soapJson);
        return soapDocument;
    }

    public void equalAssertion(List<String> uiList, List<String> dbList, String logMessage) {
        if (dbList != null) {
            for (int i = 0; i < dbList.size(); i++) {
                if (dbList.get(i).equals(uiList.get(i))) {
                    AtuReports.passResults("Equal Assertion Test", "--", logMessage + "in DB: " + dbList.get(i),
                            logMessage + "in UI: " + uiList.get(i));
                    Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + "in DB: " + dbList.get(i),
                            logMessage + "in UI: " + uiList.get(i));
                } else {
                    Reporter.LogEvent(TestStatus.FAIL, "Equal Assertion Test Fails",
                            logMessage + "in DB: " + dbList.get(i), logMessage + "in UI: " + uiList.get(i));
                    AtuReports.failResults("Equal Assertion Test Fails", "--", logMessage + "in DB: " + dbList.get(i),
                            logMessage + "in UI: " + uiList.get(i));
                }
            }
        } else {
            AtuReports.passResults("Equal Assertion Test", "--", logMessage + "in DB: " + dbList,
                    logMessage + "in UI: " + uiList);
            Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + "in DB: " + dbList,
                    logMessage + "in UI: " + uiList);
        }
    }

    public void equalAssertionInt(List<String> uiList, List<Integer> dbList, String logMessage) {
        if (dbList != null) {
            for (int i = 0; i < dbList.size(); i++) {
                if (dbList.get(i).toString().equals(uiList.get(i))) {
                    AtuReports.passResults("Equal Assertion Test", "--", logMessage + "in DB: " + dbList.get(i),
                            logMessage + "in UI: " + uiList.get(i));
                    Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + "in DB: " + dbList.get(i),
                            logMessage + "in UI: " + uiList.get(i));
                } else {
                    Reporter.LogEvent(TestStatus.FAIL, "Equal Assertion Test Fails",
                            logMessage + "in DB: " + dbList.get(i), logMessage + "in UI: " + uiList.get(i));
                    AtuReports.failResults("Equal Assertion Test Fails", "--", logMessage + "in DB: " + dbList.get(i),
                            logMessage + "in UI: " + uiList.get(i));
                }
            }
        } else {
            AtuReports.passResults("Equal Assertion Test", "--", logMessage + "in DB: " + dbList,
                    logMessage + "in UI: " + uiList);
            Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + "in DB: " + dbList,
                    logMessage + "in UI: " + uiList);
        }
    }

    public void equalAssertionSingle(String name, String uiValue, String dbValue, String logMessage) {
        if (dbValue != null) {
            // for(int i =0; i<dbValue.length(); i++){
            if (dbValue.equals(uiValue)) {
                AtuReports.passResults("Equal Assertion Test", name, logMessage + " in DB: " + dbValue,
                        logMessage + " in UI: " + uiValue);

                Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + " in DB: " + dbValue,
                        logMessage + " in UI: " + uiValue);
            } else {
                Reporter.LogEvent(TestStatus.FAIL, "Equal Assertion Test Fails", logMessage + " in DB: " + dbValue,
                        logMessage + " in UI: " + uiValue);

                AtuReports.failResults("Equal Assertion Test Fails", name, logMessage + " in DB: " + dbValue,
                        logMessage + " in UI: " + uiValue);
            }

            // }
        } else {
            AtuReports.passResults("Equal Assertion Test", name, logMessage + " in DB: " + dbValue,
                    logMessage + " in UI: " + uiValue);

            Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + " in DB: " + dbValue,
                    logMessage + " in UI: " + uiValue);
        }
    }

    public void equalAssertionIntAPI(String name, int actual, int expected, String logMessage) {
        // "Selection Page: MarketList API", 200, subscribedMarket.statusCode(),
        // "Market list Call"
        if (actual == expected) {
            AtuReports.passResults("Equal Assertion Test", name, "Expected Value of " + logMessage + " = " + expected,
                    "Actual Value of " + logMessage + " = " + actual);

            Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test",
                    "Expected Value of " + logMessage + " = " + expected,
                    "Actual Value of " + logMessage + " = " + actual);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Equal Assertion Test Fails",
                    "Expected Value of " + logMessage + " = " + expected,
                    "Actual Value of " + logMessage + " = " + actual);

            AtuReports.failResults("Equal Assertion Test Fails", name,
                    "Expected Value of " + logMessage + " = " + expected,
                    "Actual Value of " + logMessage + " = " + actual);
        }
    }

    public void assertSuccessStatus(int actual, String logMessage) {
        if (actual == 200) {
            AtuReports.passResults("Verify " + logMessage, "--", logMessage + " Call Status should be 200",
                    logMessage + " Call Status is " + actual);

            Reporter.LogEvent(TestStatus.PASS, "Verify " + logMessage, logMessage + " Call Status should be 200",
                    logMessage + " Call Status is " + actual);
        } else {
            Reporter.LogEvent(TestStatus.FAIL, "Verify " + logMessage, logMessage + " Call Status should be 200",
                    logMessage + " Call Status is " + actual);
            AtuReports.failResults("Verify " + logMessage, "--", logMessage + " Call Status should be 200",
                    logMessage + " Call Status is " + actual);
        }
    }

    public void equalAssertionDouble(String name, Double uiValue, BigDecimal dbValue, String logMessage) {
        if (dbValue != null) {
            for (int i = 0; i < dbValue.doubleValue(); i++) {
                if (dbValue.doubleValue() == uiValue) {
                    AtuReports.passResults("Equal Assertion Test", name, logMessage + "in DB: " + dbValue,
                            logMessage + "in UI: " + uiValue);
                    Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + "in DB: " + dbValue,
                            logMessage + "in UI: " + uiValue);
                } else {
                    Reporter.LogEvent(TestStatus.FAIL, "Equal Assertion Test Fails", logMessage + "in DB: " + dbValue,
                            logMessage + "in UI: " + uiValue);
                    AtuReports.failResults("Equal Assertion Test Fails", name, logMessage + "in DB: " + dbValue,
                            logMessage + "in UI: " + uiValue);
                }
            }
        } else {
            AtuReports.passResults("Equal Assertion Test", name, logMessage + "in DB: " + dbValue,
                    logMessage + "in UI: " + uiValue);
            Reporter.LogEvent(TestStatus.PASS, "Equal Assertion Test", logMessage + "in DB: " + dbValue,
                    logMessage + "in UI: " + uiValue);
        }
    }

    public int getStatusCode(Response response) {
        return response.statusCode();
    }

}
