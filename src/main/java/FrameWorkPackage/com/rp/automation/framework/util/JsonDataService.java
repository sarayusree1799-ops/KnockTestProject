package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.*;

public class JsonDataService {
    public Iterator<Object[]> readTestDataFromJson(String sJsonFileName, String testName) {
        String testDataFolderPath = Paths.get(System.getProperty("user.dir"), (String) WebDriverBase.context.getBean("rot_folder")).toString();
        List<Hashtable<String, String>> testData = null;
        for (File testDataFile : (new File(testDataFolderPath)).listFiles()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonObjectData = null;
            if (testDataFile.getPath().contains(sJsonFileName)) {
                try {
                    JsonNode jsonNode = objectMapper.readTree(testDataFile);
                    jsonObjectData = jsonNode.toString();
                    System.out.println(jsonNode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                testData = this.getJsonData(jsonObjectData, testName);
            }
        }
        return this.getIterator(testData);
    }

    private Iterator<Object[]> getIterator(List<Hashtable<String, String>> testDataList) {
        List<Object[]> iteratorList = new ArrayList<>();
        for (Map map : testDataList) {
            try {
                if (map.containsKey("RunIteration") && !map.get("RunIteration").toString().equalsIgnoreCase("Yes")) {
                    System.out.println("The current iteration :" + map + " is ignore as it set to not run");
                    continue;
                }
            } catch (Exception var6) {
                System.out.println("The current iteration :" + map + " is ignore as it set to black");
                continue;
            }
            iteratorList.add(new Object[]{map});
        }
        return iteratorList.iterator();
    }

    public List<Hashtable<String, String>> getJsonData(String jsonObjectData, String method) {
        Gson gson = new Gson();
        JsonObject jsonObject = (JsonObject) gson.fromJson(jsonObjectData, JsonObject.class);
        List<Hashtable<String, String>> list = new ArrayList<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            if (((String) entry.getKey()).equalsIgnoreCase(method)) {
                JsonElement jsonElement = (JsonElement) entry.getValue();
                Type type = (new TypeToken<List<Hashtable<String, String>>>() {}).getType();
                List<Hashtable<String, String>> subList = (List) gson.fromJson(jsonElement, type);
                list.addAll(subList);
            }
        }
        return list;
    }
}
