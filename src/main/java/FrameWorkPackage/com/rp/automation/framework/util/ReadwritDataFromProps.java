package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.reports.AtuReports;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

public class ReadwritDataFromProps {
    WebDriverBase base;
    Properties props;

    public ReadwritDataFromProps(WebDriverBase base) {
        try {
            FileInputStream fs = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\temp.properties");
            this.props = new Properties();
            this.props.load(fs);
        } catch (Exception var3) {
            AtuReports.failResults("Failed to load properties file", "--", "Properties file handling failed", "Properties file handling failed");
        }
        this.base = base;
    }

    public void writeDynamicData(String key, String value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\temp.properties");
            this.props.setProperty(key, value);
            this.props.store(output, (String)null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException var12) {
                    AtuReports.failResults("Failed to write to file", "--", "Failed to write to file", "Failed to write to file");
                }
            }
        }
    }

    public void writeDynamicData(String[] key, String[] value) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\temp.properties");
            for(int i = 0; i < key.length; ++i) {
                this.props.setProperty(key[i], value[i]);
            }
            this.props.store(output, (String)null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try { output.close();
                } catch (IOException var12) {
                    AtuReports.failResults("Failed to write to file", "--", "Failed to write to file", "Failed to write to file");
                }
            }
        }
    }

    public HashMap<String, String> readPropertiesFile() {
        InputStream input = null;
        HashMap<String, String> data = new HashMap<>();
        try {
            input = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\temp.properties");
            for(String key : this.props.stringPropertyNames()) {
                System.out.println(key + ":" + this.props.getProperty(key));
                data.put(key, this.props.getProperty(key));
            }
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException var13) {
                    AtuReports.failResults("Failed to write to file", "--", "Failed to write to file", "Failed to write to file");
                }
            }
        }
        return data;
    }
}
