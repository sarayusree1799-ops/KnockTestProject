package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.BoundRequestBuilder;
import org.asynchttpclient.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.ExecutionException;


public class MailTrapUtils {
    public MailTrapUtils() {
    }

    public void testkk() {
        System.out.println("testkk");
    }

    public String getEmailContentasURLBySubject(String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String apiToken = "b7e0e75c944e72d76a56e3392c0a23b";
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Response inboxesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }
            String inboxesResponseBody = inboxesResponse.getResponseBody();
            JSONArray inboxes = new JSONArray(inboxesResponseBody);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            Response inboxMessagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxMessagesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }
            String inboxMessagesResponseBody = inboxMessagesResponse.getResponseBody();
            JSONArray messages = new JSONArray(inboxMessagesResponseBody);
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
                    Response messagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io" + messageHtmlPath).setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
                    if (messagesResponse.getStatusCode() != 200) {
                        System.out.println("Cannot find message" + messageId);
                        System.exit(0);
                    }
                    String messagesResponseBody = messagesResponse.getResponseBody();
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
                    break;
                }
            }
            client.close();
            return msgURL;
        } catch (InterruptedException var27) {
            throw new RuntimeException(var27);
        } catch (ExecutionException var28) {
            throw new RuntimeException(var28);
        } catch (JSONException var29) {
            throw new RuntimeException(var29);
        } catch (IOException var30) {
            throw new RuntimeException(var30);
        }
    }

    public String getEmailContentasTextBySubject(String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String apiToken = "b7e0e75c944e72d76a56e3392c0a23b";
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Response inboxesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }
            String inboxesResponseBody = inboxesResponse.getResponseBody();
            JSONArray inboxes = new JSONArray(inboxesResponseBody);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            Response inboxMessagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxMessagesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }
            String inboxMessagesResponseBody = inboxMessagesResponse.getResponseBody();
            JSONArray messages = new JSONArray(inboxMessagesResponseBody);
            String messagesResponseBody = null;
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
                    Response messagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io" + messageHtmlPath).setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
                    if (messagesResponse.getStatusCode() != 200) {
                        System.out.println("Cannot find message" + messageId);
                        System.exit(0);
                    }
                    messagesResponseBody = messagesResponse.getResponseBody();
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
                    break;
                }
            }
            client.close();
            return messagesResponseBody;
        } catch (InterruptedException var27) {
            throw new RuntimeException(var27);
        } catch (ExecutionException var28) {
            throw new RuntimeException(var28);
        } catch (JSONException var29) {
            throw new RuntimeException(var29);
        } catch (IOException var30) {
            throw new RuntimeException(var30);
        }
    }

    public String getEmailContentasURLBySubject2(String apiToken, String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Response inboxesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }
            String inboxesResponseBody = inboxesResponse.getResponseBody();
            JSONArray inboxes = new JSONArray(inboxesResponseBody);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            Response inboxMessagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxMessagesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }
            String inboxMessagesResponseBody = inboxMessagesResponse.getResponseBody();
            JSONArray messages = new JSONArray(inboxMessagesResponseBody);
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
                    Response messagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io" + messageHtmlPath).setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
                    if (messagesResponse.getStatusCode() != 200) {
                        System.out.println("Cannot find message" + messageId);
                        System.exit(0);
                    }
                    String messagesResponseBody = messagesResponse.getResponseBody();
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
                    break;
                }
            }
            client.close();
            return msgURL;
        } catch (InterruptedException var27) {
            throw new RuntimeException(var27);
        } catch (ExecutionException var28) {
            throw new RuntimeException(var28);
        } catch (JSONException var29) {
            throw new RuntimeException(var29);
        } catch (IOException var30) {
            throw new RuntimeException(var30);
        }
    }

    public String getEmailContentasTextBySubject(String apiToken, String subjectExpected) {
        try {
            System.out.println("getEmailContentBySubjectAndBody");
            String accountId = "136829";
            String mailtrap = "https://mailtrap.io";
            String msgURL = "";
            AsyncHttpClient client = new DefaultAsyncHttpClient();
            Response inboxesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inboxes for the account");
                System.exit(0);
            }
            String inboxesResponseBody = inboxesResponse.getResponseBody();
            JSONArray inboxes = new JSONArray(inboxesResponseBody);
            String inboxId = inboxes.getJSONObject(0).getBigInteger("id").toString();
            Response inboxMessagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io/api/accounts/136829/inboxes/" + inboxId + "/messages").setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
            if (inboxMessagesResponse.getStatusCode() != 200) {
                System.out.println("Cannot find inbox" + inboxId);
                System.exit(0);
            }
            String inboxMessagesResponseBody = inboxMessagesResponse.getResponseBody();
            JSONArray messages = new JSONArray(inboxMessagesResponseBody);
            String messagesResponseBody = null;
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
                    Response messagesResponse = (Response) ((BoundRequestBuilder) (BoundRequestBuilder) client.prepare("GET", "https://mailtrap.io" + messageHtmlPath).setHeader("Content-Type", "application/json").setHeader("Api-Token", apiToken)).execute().get();
                    if (messagesResponse.getStatusCode() != 200) {
                        System.out.println("Cannot find message" + messageId);
                        System.exit(0);
                    }
                    messagesResponseBody = messagesResponse.getResponseBody();
                    msgURL = "https://mailtrap.io" + messageHtmlPath + "?api_token=" + apiToken;
                    break;
                }
            }
            client.close();
            return messagesResponseBody;
        } catch (InterruptedException var27) {
            throw new RuntimeException(var27);
        } catch (ExecutionException var28) {
            throw new RuntimeException(var28);
        } catch (JSONException var29) {
            throw new RuntimeException(var29);
        } catch (IOException var30) {
            throw new RuntimeException(var30);
        }
    }
}