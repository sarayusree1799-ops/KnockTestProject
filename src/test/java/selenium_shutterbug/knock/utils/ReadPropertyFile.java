package selenium_shutterbug.knock.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadPropertyFile {

    public static String getProperty(String env, String key) {
        Properties properties;
        try {
            FileInputStream file = new FileInputStream("src/test/resources/env/" + env + ".properties");
            properties = new Properties();
            properties.load(file);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException("Failed to load " + env + " properties file");
        }
        return properties.getProperty(key);
    }
}