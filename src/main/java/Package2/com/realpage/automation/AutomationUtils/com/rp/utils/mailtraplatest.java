package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.time.Instant;
import java.time.ZonedDateTime;

public class mailtraplatest {
    public HttpClient httpClient = HttpClientBuilder.create().build();
    final String apiToken = "b7e0e875c944e72d76a56e3392c0a23b";

    public mailtraplatest() {
    }

    public String getEmailContentasURLBySubject(String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectandBody");
            String apiToken = "b7e0e875c944e72d76a56e3392c0a23b";
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            HttpGet getRequestInbox = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes/");
            HttpResponse inboxesResponse = this.httpClient.execute(this.setRequestHeader(getRequestInbox));
            String json_string = EntityUtils.toString(inboxesResponse.getEntity());
            System.out.println("Out is - " + json_string);
            if (inboxesResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }

            JSONArray inboxes = new JSONArray(json_string);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            HttpGet getRequestMsg = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages/");
            HttpResponse msgResponse = this.httpClient.execute(this.setRequestHeader(getRequestMsg));
            json_string = EntityUtils.toString(msgResponse.getEntity());
            System.out.println("Out is - " + json_string);
            if (msgResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inbox " + inboxId);
                System.exit(0);
            }

            JSONArray messages = new JSONArray(json_string);

            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("is_read");
                ZonedDateTime parsedDate = ZonedDateTime.parse(jsonObject.getString("created_at"));
                Instant instant = parsedDate.toInstant();
                Date dateAct = Date.from(instant);
                Instant oldDateTime = Instant.now().minusSeconds(49180L);
                Date oldDate = Date.from(oldDateTime);
                if (subject.replace(" ", "").contains(subjectExpected.replace(" ", "")) && !readFlag && dateAct.after(oldDate)) {
                    String messageId = jsonObject.getBigInteger("id").toString();
                    String messageHtmlPath = jsonObject.getString("html_path");
                    HttpGet msgContentReq = new HttpGet("https://mailtrap.io" + messageHtmlPath);
                    HttpResponse msgContentResp = this.httpClient.execute(this.setRequestHeader(msgContentReq));
                    json_string = EntityUtils.toString(msgContentResp.getEntity());
                    System.out.println("Out is = " + json_string);
                    if (msgContentResp.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Cannot find message " + messageId);
                        System.exit(0);
                    }

                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=b7e0e875c944e72d76a56e3392c0a23b";
                    break;
                }
            }
            return msgURL;
        } catch (JSONException var27) {
            throw new RuntimeException(var27);
        } catch (IOException var28) {
            throw new RuntimeException(var28);
        }
    }

    public String getEmailContentasTextBySubject(String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectandBody");
            String apiToken = "b7e0e875c944e72d76a56e3392c0a23b";
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            HttpGet getRequestInbox = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes/");
            HttpResponse inboxesResponse = this.httpClient.execute(this.setRequestHeader(getRequestInbox));
            String json_string = EntityUtils.toString(inboxesResponse.getEntity());
            System.out.println("Out is - " + json_string);
            if (inboxesResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }

            JSONArray inboxes = new JSONArray(json_string);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            HttpGet getRequestMsg = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages/");
            HttpResponse msgResponse = this.httpClient.execute(this.setRequestHeader(getRequestMsg));
            json_string = EntityUtils.toString(msgResponse.getEntity());
            System.out.println("Out is - " + json_string);
            if (msgResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inbox " + inboxId);
                System.exit(0);
            }

            JSONArray messages = new JSONArray(json_string);

            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("is_read");
                ZonedDateTime parsedDate = ZonedDateTime.parse(jsonObject.getString("created_at"));
                Instant instant = parsedDate.toInstant();
                Date dateAct = Date.from(instant);
                Instant oldDateTime = Instant.now().minusSeconds(49180L);
                Date oldDate = Date.from(oldDateTime);
                if (subject.replace(" ", "").contains(subjectExpected.replace(" ", "")) && !readFlag && dateAct.after(oldDate)) {
                    String messageId = jsonObject.getBigInteger("id").toString();
                    String messageHtmlPath = jsonObject.getString("html_path");
                    HttpGet msgContentReq = new HttpGet("https://mailtrap.io" + messageHtmlPath);
                    HttpResponse msgContentResp = this.httpClient.execute(this.setRequestHeader(msgContentReq));
                    json_string = EntityUtils.toString(msgContentResp.getEntity());
                    System.out.println("Out is = " + json_string);
                    if (msgContentResp.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Cannot find message " + messageId);
                        System.exit(0);
                    }

                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=b7e0e875c944e72d76a56e3392c0a23b";
                    break;
                }
            }
            return msgURL;
        } catch (JSONException var27) {
            throw new RuntimeException(var27);
        } catch (IOException var28) {
            throw new RuntimeException(var28);
        }
    }

    public String getEmailContentasTextBySubject(String subjectExpected, String inboxid) throws IOException {
        try {
            String apiToken = "";
            String accountId = "136829";
            if (inboxid.equalsIgnoreCase("2463231")) {
                apiToken = "832142d11f8b9b902d9fba6517f7e71b";
            } else {
                apiToken = "b7e0e875c944e72d76a56e3392c0a23b";
            }

            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            HttpGet getRequestInbox = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes/");
            getRequestInbox.setHeader("Content-Type", "application/json");
            getRequestInbox.setHeader("Authorization", "Bearer " + apiToken);
            HttpClient client = HttpClients.custom().build();
            HttpUriRequest request = RequestBuilder.get().setUri("https://mailtrap.io/api/accounts/136829/inboxes/")
                    .setHeader("Content-Type", "application/json").setHeader("Authorization", "Bearer " + apiToken).build();
            HttpResponse inboxesResponse = client.execute(request);
            String json_string = EntityUtils.toString(inboxesResponse.getEntity());
            System.out.println("Inbox list is - " + json_string);
            if (inboxesResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }

            JSONArray inboxes = new JSONArray(json_string);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            HttpGet getRequestMsg = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages/");
            HttpResponse msgResponse = client.execute(this.setRequestHeader(getRequestMsg));
            json_string = EntityUtils.toString(msgResponse.getEntity());
            System.out.println("Out is = " + json_string);
            if (msgResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inbox " + inboxId);
                System.exit(0);
            }

            JSONArray messages = new JSONArray(json_string);

            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("is_read");
                ZonedDateTime parsedDate = ZonedDateTime.parse(jsonObject.getString("created_at"));
                Instant instant = parsedDate.toInstant();
                Date dateAct = Date.from(instant);
                Instant oldDateTime = Instant.now().minusSeconds(49180L);
                Date oldDate = Date.from(oldDateTime);
                if (subject.replace(" ", "").contains(subjectExpected.replace(" ", "")) && !readFlag && dateAct.after(oldDate)) {
                    String messageId = jsonObject.getBigInteger("id").toString();
                    String messageHtmlPath = jsonObject.getString("html_path");
                    HttpGet msgContentReq = new HttpGet("https://mailtrap.io" + messageHtmlPath);
                    HttpResponse msgContentResp = this.httpClient.execute(this.setRequestHeader(msgContentReq));
                    json_string = EntityUtils.toString(msgContentResp.getEntity());
                    System.out.println("Out is = " + json_string);
                    if (msgContentResp.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Cannot find message " + messageId);
                        System.exit(0);
                    }
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=b7e0e875c944e72d76a56e3392c0a23b";
                    break;
                }
            }
            return json_string;
        } catch (JSONException var30) {
            throw new RuntimeException(var30);
        } catch (IOException var31) {
            throw new RuntimeException(var31);
        }
    }

    private HttpGet setRequestHeader(HttpGet requestHeader) {
        requestHeader.setHeader("Content-Type", "application/json");
        requestHeader.setHeader("Application", "application/json");
        requestHeader.setHeader("Api-Token", "b7e0e875c944e72d76a56e3392c0a23b");
        return requestHeader;
    }
}