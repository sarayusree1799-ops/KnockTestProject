package com.knock.ui.base;

import FrameWorkPackage.com.rp.automation.framework.util.ExcelService;
import FrameWorkPackage.com.rp.automation.framework.util.JsonDataService;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class BaseTest extends WebDriverBase {

    public static Integer getRandomJsonArrayIndex(JSONArray jsonArray) {
        Random random = new Random();
        return random.nextInt(jsonArray.length());
    }

    // ****************** readTestDataFromExcel**************************
    @DataProvider(name = "TestData") // ,parallel = true)
    public Iterator<Object[]> dataProvider(Method method) {
        System.out.println("counter===" + counter++);
        return new ExcelService().readTestDataFromExcel(getBeanName("TestDataWorkBookName"), "TestData", method.getName());
    }

    // ****************** readTestDataFromJson**************************
    @DataProvider(name = "JsonTestData") // , parallel = true)
    public Iterator<Object[]> readTestDataJson(Method method) {
        System.out.println("counter===" + counter++);
        return new JsonDataService().readTestDataFromJson(getBeanName("TestDataWorkBookName"), method.getName());
    }

    public JSONObject readPayloadFromJson(String payloadJson) {
        try {
            File file = new File(System.getProperty("user.dir") + "/TestData/" + payloadJson);
            String payloadTxt = FileUtils.readFileToString(file, String.valueOf(StandardCharsets.UTF_8));
            return new JSONObject(payloadTxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONArray readPayloadFromJsonArray(String payloadJson) {
        try {
            File file = new File(System.getProperty("user.dir") + "/TestData/" + payloadJson);
            String payloadTxt = FileUtils.readFileToString(file, String.valueOf(StandardCharsets.UTF_8));
            return new JSONArray(payloadTxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> convertListObjectToString(List<Object> objList) {
        List<String> stringList = new ArrayList<>(objList.size());
        for (Object obj : objList) {
            stringList.add(Objects.toString(obj, null));
        }
        return stringList;
    }
}