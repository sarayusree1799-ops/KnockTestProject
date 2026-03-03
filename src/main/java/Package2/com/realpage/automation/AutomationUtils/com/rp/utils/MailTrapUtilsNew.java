package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;

public class MailTrapUtilsNew {
    public MailTrapUtilsNew() {
    }

    public HttpClient httpClient = HttpClientBuilder.create().build();
    final String apiToken = "b7e0e75c944e72d76a56e3392c0a23b";

    public String getEmailContentasURLBySubject(String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String apiToken = "b7e0e75c944e72d76a56e3392c0a23b";
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
            if (msgResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }

            JSONArray messages = new JSONArray(json_string);

            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("redFlag");
                ZonedDateTime parsedDate = ZonedDateTime.parse(jsonObject.getString("created_at"));
                Instant instant = parsedDate.toInstant();
                Date dateAct = Date.from(instant);
                Instant oldDateTime = Instant.now().minusSeconds(300L);
                Date oldDate = Date.from(oldDateTime);
                if (subject.replace(" ", "").contains(subjectExpected.replace(" ", "")) && !readFlag && dateAct.after(oldDate)) {
                    String messageId = jsonObject.getBigInteger("id").toString();
                    String messageHtmlPath = jsonObject.getString("html_path");
                    HttpGet msgContentReq = new HttpGet("https://mailtrap.io" + messageHtmlPath);
                    HttpResponse msgContentResp = this.httpClient.execute(this.setRequestHeader(msgContentReq));
                    if (msgContentResp.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Cannot find message" + messageId);
                        System.exit(0);
                    }

                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
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
            System.out.println("getEmailContentBySubjectAndBody");
            String apiToken = "b7e0e75c944e72d76a56e3392c0a23b";
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
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }

            JSONArray messages = new JSONArray(json_string);

            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("redFlag");
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
                    System.out.println("Out is - " + json_string);
                    HttpGet msgAttachmentReq = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes" + inboxId + "/messages/" + messageId);
                    HttpResponse msgAttachmentRes = this.httpClient.execute(this.setRequestHeader(msgAttachmentReq));
                    json_string = EntityUtils.toString(msgAttachmentRes.getEntity());
                    System.out.println("Out is - " + json_string);
                    JSONArray attachments = new JSONArray(json_string);
                    for (int k = 0; k < attachments.length(); ++k) {
                        JSONObject attachment = attachments.getJSONObject(k);
                        String attachmentId = attachment.getString("id");
                        String fileName = attachment.getString("filename");
                        HttpGet attachDownloadReq = new HttpGet("https://mailtrap.io/api/accounts/136829/inboxes" + inboxId + "/messages/" + messageId + "/attachments/" + attachmentId + "/download");
                        HttpResponse atttachDownloadRes = this.httpClient.execute(this.setRequestHeader(attachDownloadReq));
                        InputStream inputStream = atttachDownloadRes.getEntity().getContent();
                        File file = new File("C:\\" + fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            fos.write(buffer, 0, bytesRead);
                        }
                        fos.close();
                    }
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
                    break;
                }
            }
            return json_string;
        } catch (JSONException var41) {
            throw new RuntimeException(var41);
        } catch (IOException var42) {
            throw new RuntimeException(var42);
        }
    }

    public String getEmailContentasTextBySubject(String subjectExpected, String inboxid) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String apiToken = "b7e0e75c944e72d76a56e3392c0a23b";
            String accountId = "136829";
            if (inboxid.equalsIgnoreCase("2463231")) {
                apiToken = "8321421d11f89b902d9fba6517f7e71b";
            } else {
                apiToken = "b7e0e75c944e72d76a56e3392c0a23b";
            }
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
            if (msgResponse.getStatusLine().getStatusCode() != 200) {
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }

            JSONArray messages = new JSONArray(json_string);

            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("redFlag");
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
                    if (msgContentResp.getStatusLine().getStatusCode() != 200) {
                        System.out.println("Cannot find message" + messageId);
                        System.exit(0);
                    }

                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
                    break;
                }
            }
            return json_string;
        } catch (JSONException var28) {
            throw new RuntimeException(var28);
        } catch (IOException var29) {
            throw new RuntimeException(var29);
        }
    }

    private HttpGet setRequestHeader(HttpGet requestHeader) {
        requestHeader.setHeader("Content-Type", "application/json");
        requestHeader.setHeader("Accept", "application/json");
        requestHeader.setHeader("Authorization", "Bearer " + apiToken);
        return requestHeader;
    }
}