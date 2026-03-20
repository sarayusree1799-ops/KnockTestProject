package com.knock.utils;

import com.knock.ui.base.BaseTest;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import com.twilio.type.Twiml;
import org.json.JSONObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;


public class Twilio {

    public static JSONObject twilioTestData = new BaseTest().readPayloadFromJson("twilioTestData.json");

    public static void makeInboundCall(JSONObject credentials, String to) {
        try {
            com.twilio.Twilio.init(credentials.get("accountSid").toString(), credentials.get("authToken").toString());
            Call call = Call.creator(
                    //to
                    new PhoneNumber(to),
                    //from
                    new PhoneNumber(credentials.get("senderPhoneNum").toString()),
                    new Twiml(twilioTestData.get("twiml").toString())
            ).create();
        } catch (Exception e) {
            throw e;
        }
    }

    public static JSONObject getCredentials() {
        String secretName = "twillioCred.json";
        Region region = Region.of("us-west-2");

        // Create a Secrets Manager client
        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();
        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
        } catch (Exception e) {
            throw e;
        }

        String secret = getSecretValueResponse.secretString();
        return new JSONObject(secret);
    }
}