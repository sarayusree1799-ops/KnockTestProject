package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.util.EntityUtils;
import org.testng.ITestResult;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

public class AzureUtils {

    public HttpClient httpClient = HttpClientBuilder.create().build();
    public String organizationUrl = "htttps://tfs.realpage.com/tfs/RealPage";

    public String pAToken = "ajbeg4rjbrflxhy6kowm4ztikqrtwikiozspbvj6emn74rknq3ca";

    public String AuthStr;
    public Base64 base64;
    public String encodedPAT;
    public String suiteidFinal;
    public String statusFlag;

    public AzureUtils() {
        this.AuthStr = ":" + this.pAToken;
        this.base64 = new Base64();
        this.encodedPAT = new String(this.base64.encode(this.AuthStr.getBytes()));
        this.suiteidFinal = "";
        this.statusFlag = "";
    }

    public void updateTestStatus(String projectName, String planId, String suiteId, String testCaseId, String executionStatus) {
        try {
            if (!planId.contains("NoParameters") && suiteId.contains("NoParameters")) {
                String[] azureIds = testCaseId.split(",");
                if (azureIds.length > 1) {
                    String[] var7 = azureIds;
                    int var8 = azureIds.length;

                    for (int var9 = 0; var9 < var8; ++var9) {
                        String azuTestCaseId = var7[var9];
                        this.getTestCasePoints(projectName, planId, suiteId, azuTestCaseId, executionStatus);
                    }
                } else {
                    this.getTestCasePoints(projectName, planId, suiteId, testCaseId, executionStatus);
                }
            }
        } catch (JsonProcessingException var11) {
            throw new RuntimeException(var11);
        } catch (ClientProtocolException var12) {
            throw new RuntimeException(var12);
        } catch (IOException var13) {
            throw new RuntimeException(var13);
        }
    }

    public void updateTestCaseExecution(String projectName, String planId, String suiteId, String testCaseId, String executionStatus) throws ClientProtocolException, IOException{
        if (!planId.contains("NoParameters") && !suiteId.contains("NoParameters")) {
            String[] azureIds = testCaseId.split(",");
            if (azureIds.length > 1) {
                String[] var7 = azureIds;
                int var8 = azureIds.length;

                for (int var9 = 0; var9 < var8; ++var9) {
                    String azuTestCaseId = var7[var9];
                    this.getTestCasePoints(projectName, planId, suiteId, azuTestCaseId, executionStatus);
                }
            } else {
                this.getTestCasePoints(projectName, planId, suiteId, testCaseId, executionStatus);
            }
        }
    }

    public void getTestCasePoints(String projectName, String planId, String suiteId, String testCaseId, String executionStatus) throws IOException, ClientProtocolException, JsonProcessingException {
        this.searchTestCaseInSuite(projectName, planId, suiteId, testCaseId);
        if (this.statusFlag != "OK") {
            System.out.println("Test case not found in provided Suite");
        } else {
            HttpGet getRequest = new HttpGet(this.organizationUrl + projectName + "/_apis/test/Plans/" + planId + "/Suites/" + this.suiteidFinal + "/points?testCaseId=" + testCaseId + "&api-version=5.0");
            HttpResponse response = this.httpClient.execute(this.setRequestHeader(getRequest));
            String json_string = EntityUtils.toString(response.getEntity());
            System.out.println("Out is = " + json_string);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNodes = objectMapper.readTree(json_string);
            JsonNode valueJsonNodes = jsonNodes.get("value");
            System.out.println("valueJsonNodes : " + valueJsonNodes.size());
            Iterator var12 = valueJsonNodes.iterator();

            while(var12.hasNext()) {
                JsonNode jsonNode = (JsonNode)var12.next();
                System.out.println("n.get(0) " + jsonNode.get("id"));
                this.updateTestPoint(projectName, planId, this.suiteidFinal, jsonNode.get("id").toString(), executionStatus);
            }
        }
    }

    public void updateTestPoint(String projectName, String planId, String suiteId, String points, String outcome) throws ClientProtocolException, IOException {
        HttpPatch patchRequest = new HttpPatch(this.organizationUrl + projectName + "/_apis/test/Plans/" + planId + "/Suites/" + suiteId + "/points/" + points + "?api-version=5.0");
        String reqbody = "{\"outcome\": \"" + outcome + "\" }";
        System.out.println("reqbody " + reqbody);
        HttpResponse response1 = this.httpClient.execute(this.setRequestHeader(patchRequest, reqbody));
        String json_string1 = EntityUtils.toString(response1.getEntity());
        System.out.println("patchRequest Out is - " + json_string1);
    }

    private HttpPatch setRequestHeader(HttpPatch requestHeader, String reqbody) {
        requestHeader.setHeader("Content-Type", "application/json");
        requestHeader.setHeader("Accept", "application/json");
        requestHeader.setHeader("Authorization", "Basic " + this.encodedPAT);
        requestHeader.setEntity(new StringEntity(reqbody, ContentType.APPLICATION_JSON));
        return requestHeader;
    }

    private HttpGet setRequestHeader(HttpGet requestHeader) {
        requestHeader.setHeader("Content-Type", "application/json");
        requestHeader.setHeader("Accept", "application/json");
        requestHeader.setHeader("Authorization", "Basic " + this.encodedPAT);
        return requestHeader;
    }

    public void updateTestCaseExecution(ITestResult results) throws ClientProtocolException, IOException {
        try {
            String planId = System.getProperty("suiteid");
            String suiteId = System.getProperty("suiteid");
            if (suiteId == null || suiteId.isEmpty()) {
                planId = results.getTestContext().getSuite().getParameter("planid");
                suiteId = results.getTestContext().getSuite().getParameter("suiteid");
            }

            if (results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(UpdateTestStatusTFS.class) != null &&
                    results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(UpdateTestStatusTFS.class) != null) {
                String projectName =
                        ((UpdateTestStatusTFS)results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(UpdateTestStatusTFS.class)).productName();
                String azureId =
                        ((UpdateTestStatusTFS)results.getMethod().getConstructorOrMethod().getMethod().getAnnotation(UpdateTestStatusTFS.class)).testCaseIds();
                String[] azureIds = azureId.split(",");
                if (projectName != null && azureId != null && !planId.contains("NoParameters") && !suiteId.isEmpty()) {
                    System.out.println("azureIds.length " + azureIds.length);
                    if (azureIds.length > 1) {
                        String[] var7 = azureIds;
                        int var8 = azureIds.length;

                        for(int var9 = 0; var9 < var8; ++var9) {
                            String azuTestCaseId = var7[var9];
                            this.getTestCasePointsTagLevel(projectName, planId, suiteId, azuTestCaseId, String.valueOf(results.getStatus()));
                        }
                    } else {
                        this.getTestCasePointsTagLevel(projectName, planId, suiteId, azureIds[0], String.valueOf(results.getStatus()));
                    }
                }
            }
        } catch (IOException var11) {
            var11.printStackTrace();
        }
    }

    private void getTestCasePointsTagLevel(String projectName, String planId, String suiteId, String testCaseId, String executionStatus) throws IOException, ClientProtocolException, JsonProcessingException {
        this.statusFlag = "";
        this.searchTestCaseInSuite(projectName, planId, suiteId, testCaseId);
        if (this.statusFlag != "OK") {
            System.out.println("Test case not found in provided Suite");
        } else {
            HttpGet getRequest = new HttpGet(this.organizationUrl + projectName + "/_apis/test/Plans/" + planId + "/Suites/" + this.suiteidFinal + "/points?testCaseId=" + testCaseId + "&api-version=5.0");
            HttpResponse response = this.httpClient.execute(this.setRequestHeader(getRequest));
            String json_string = EntityUtils.toString(response.getEntity());
            System.out.println("Out is" + json_string);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNodes = objectMapper.readTree(json_string);
            JsonNode valueJsonNodes = jsonNodes.get("value");
            System.out.println("valueJsonNodes : " + valueJsonNodes.size());
            if (executionStatus.toString().equalsIgnoreCase("1")) {
                executionStatus = "passed";
            } else if (executionStatus.toString().equalsIgnoreCase("2")) {
                executionStatus = "failed";
            }

            System.out.println("executionStatus : " + executionStatus);
            Iterator var12 = valueJsonNodes.iterator();

            while(var12.hasNext()) {
                JsonNode jsonNode = (JsonNode)var12.next();
                System.out.println("n.get(0)" + jsonNode.get("id"));
                this.updateTestPoint(projectName, planId, suiteId, jsonNode.get("id").toString(), executionStatus);
            }
        }
    }

    public void searchTestCaseInSuite(String projectName, String planId, String suiteId, String testCaseId) {
        try {
            HttpGet currentSuiteUrl = new HttpGet(this.organizationUrl + projectName + "/_apis/test/Plans/" + planId + "/Suites/" + suiteId + "/points?testCaseId=" + testCaseId + "&api-version=5.0");
            HttpResponse response = this.httpClient.execute(this.setRequestHeader(currentSuiteUrl));
            String json_string = EntityUtils.toString(response.getEntity());
            if (json_string.contains("\"id\"")) {
                this.suiteidFinal = suiteId;
                this.statusFlag = "OK";
            } else {
                HttpGet subSuiteUrl = new HttpGet(this.organizationUrl + projectName + "/_apis/test/Plans/" + planId + "/Suites/" + suiteId + "?$expand=1&api-version=5.0");
                HttpResponse subSuitesResponse = this.httpClient.execute(this.setRequestHeader(subSuiteUrl));
                String subSuite = EntityUtils.toString(subSuitesResponse.getEntity());
                if (subSuite.contains("\"suites\":")) {
                    String[] temp = subSuite.split("\"suites\":");
                    String[] subFolders = temp[1].split("\"id\"");
                    String[] var13 = subFolders;
                    int var14 = subFolders.length;

                    for(int var15 = 0; var15 < var14; ++var15) {
                        String subFolder = var13[var15];
                        if (this.statusFlag == "OK") {
                            break;
                        }

                        if (subFolder.contains("\"name\"")) {
                            String subSuiteId = subFolder.split("\"")[1];
                            PrintStream var10000 = System.out;
                            String[] var10001 = subFolder.split("\"");
                            var10000.println("Check subfolder: " + var10001[5]);
                        }
                    }
                }
            }
        } catch (ClientProtocolException var18) {
            throw new RuntimeException(var18);
        } catch (IOException var19) {
            throw new RuntimeException(var19);
        }
    }
}
