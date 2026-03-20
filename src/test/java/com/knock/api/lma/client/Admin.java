package com.knock.api.lma.client;

import com.knock.api.base.BaseTest;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Admin extends BaseTest {

    public static final Logger logger = LogManager.getLogger(Admin.class);

    public Response getAllUserAgents() {
        String baseURL = BaseTest.context.getBean("admin_internal_url", String.class);
    }
}
