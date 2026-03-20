package FrameWorkPackage.com.rp.automation.framework.util;

import org.asynchttpclient.Response;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MailTrapUtils {
    public String getEmailContentBySubject(String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String apiToken = "b7e0e875c944e72d76a56e3392c0a23b";
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Response inboxesResponse = (Response) ((BoundRequestBuilder) ((BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/").setHeader("Content-Type", "application/json")).setHeader("Api-Token", "b7e0e875c944e72d76a56e3392c0a23b")).execute().get();
            if (inboxesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }

            String inboxesResponseBody = inboxesResponse.getResponseBody();
            JSONArray inboxes = new JSONArray(inboxesResponseBody);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            Response inboxMessagesResponse = (Response) ((BoundRequestBuilder) ((BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages/").setHeader("Content-Type", "application/json")).setHeader("Api-Token", "b7e0e875c944e72d76a56e3392c0a23b")).execute().get();
            if (inboxMessagesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inbox " + inboxId);
                System.exit(0);
            }

            String inboxMessagesResponseBody = inboxMessagesResponse.getResponseBody();
            JSONArray messages = new JSONArray(inboxMessagesResponseBody);
            for (int n = 0; n < messages.length(); ++n) {
                JSONObject jsonObject = messages.getJSONObject(n);
                String subject = jsonObject.getString("subject");
                Boolean readFlag = jsonObject.getBoolean("is_read");
                ZonedDateTime parsedDate = ZonedDateTime.parse(jsonObject.getString("created_at"));
                Instant instant = parsedDate.toInstant();
                Date dateAct = Date.from(instant);
                Instant oldDateTime = Instant.now().minusSeconds(86400L);
                Date oldDate = Date.from(oldDateTime);
                if (subject.replace("\n", "").contains(subjectExpected.replace("\n", "")) && !readFlag && dateAct.after(oldDate)) {
                    String messageId = jsonObject.getString("id");
                    String messageHtmlPath = jsonObject.getString("html_path");
                    Response messageResponse = (Response) ((BoundRequestBuilder) ((BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io" + messageHtmlPath).setHeader("Content-Type", "application/json")).setHeader("Api-Token", "b7e0e875c944e72d76a56e3392c0a23b")).execute().get();
                    if (messageResponse.getStatusCode() != 200) {
                        System.out.println("Cannot find message " + messageId);
                        System.exit(0);
                    }

                    String messageResponseBody = messageResponse.getResponseBody();
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + "b7e0e875c944e72d76a56e3392c0a23b";
                    break;
                }
            }

            client.close();
            return msgURL;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
