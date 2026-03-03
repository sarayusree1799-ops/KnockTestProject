package FrameWorkPackage.com.rp.automation.framework.util;

import FrameWorkPackage.com.rp.automation.framework.webdriver.Page;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;

import java.io.*;
import java.sql.Timestamp;

import org.json.JSONObject;
import org.json.simple.parser.*;
import java.util.Map;

import net.lightbody.bmp.core.har.Har;

public class PageLoadUtil extends WebDriverBase {
    public String harJson;
    public Timestamp start_time;
    public Timestamp end_time;
    public long diff;

    public void setPageLoadStartTime(String PageName) {
        this.start_time = Page.getCurrentTime();
        server.newHar(PageName);
    }

    public void getPageLoadEndTime(Map<String, String> data, String HarFilePath, String projectName, String pageName) {
        this.getHarpFile(HarFilePath, data, pageName);
        this.end_time = Page.getcurrentTime();
        this.diff = Page.duration(this.end_time, this.start_time);
        try {
            this.setLoadTestDataRecords(projectName, pageName, this.diff, this.start_time, this.end_time,
                    Integer.parseInt((String)data.get("TestDataCode")), this.harJson);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.getMessage();
        }
    }

    public void getHarpFile(String HarFilePath, Map<String, String> data, String pageName) {
        try {
            Har har = server.getHar();
            folder = new File(HarFilePath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            File harFile = new File(HarFilePath + pageName + "_" + Integer.parseInt((String)data.get("TestDataCode")) + "_" +
                    this.start_time.getDate() + this.start_time.getTime() + ".har");
            File harpFile = new File(HarFilePath + pageName + "_" + Integer.parseInt((String)data.get("TestDataCode")) + "_" +
                    this.start_time.getDate() + this.start_time.getTime() + ".harp");
            this.harJson = null;
            this.harJson = this.getHarpJsonFile(har, harFile, harpFile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getHarpJsonfile(Har har, File harFile, File harpFile) throws IOException {
        har.getLog().toString();
        har.writeTo(harFile);
        BufferedWriter bw = null;
        FileWriter fw = null;
        String harpFileString = null;
        try {
            String content = "onInputData(";
            BufferedReader br = new BufferedReader(new FileReader(harFile));
            StringBuffer finalString = new StringBuffer();
            String sCurrentLine;
            while((sCurrentLine = br.readLine()) != null) {
                finalString.append(sCurrentLine);
            }
            StringBuilder temp = new StringBuilder();
            temp.append(content);
            temp.append(finalString.toString());
            temp.append(")");
            System.out.println("string Done");
            harFile.deleteOnExit();
            harpFileString = temp.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
                harFile.deleteOnExit();
                harpFile.deleteOnExit();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return harpFileString;
    }

    public void setLoadTestRecord(String projectName, String pageName, long duration, Timestamp start_time, Timestamp end_time) {
        DBUtil dbutil = (DBUtil)this.getContext().getBean("dbUtil");
        Object[] params = new Object[]{projectName, pageName, duration / 1000L, start_time, end_time, Page.getcurrentTime()};
        dbutil.insertRow(this.getJdbcTemplate("automationJdbcTemplate"), DBUtil.getNamedQuery("setLoadTime"), params);
    }

    public void setLoadTestDataRecord(String projectName, String pageName, long duration, Timestamp start_time, Timestamp end_time, Integer string) {
        DBUtil dbutil = (DBUtil)this.getContext().getBean("dbUtil");
        Object[] params = new Object[]{projectName, pageName, duration / 1000L, start_time, end_time, Page.getcurrentTime(), string};
        dbutil.insertRow(this.getJdbcTemplate("automationJdbcTemplate"), DBUtil.getNamedQuery("setLoadTime"), params);
    }

    public void setLoadTestDataRecords(String projectName, String pageName, long duration, Timestamp start_time, Timestamp end_time, Integer string, String harJson) {
        DBUtil dbutil = (DBUtil)this.getContext().getBean("dbUtil");
        Object[] params = new Object[]{projectName, pageName, duration / 1000L, start_time, end_time, Page.getcurrentTime(), string, harJson};
        dbutil.insertRow(this.getJdbcTemplate("automationJdbcTemplate"), DBUtil.getNamedQuery("setLoadTime"), params);
    }

    public void setLoadTestDataRecords(String projectName, String pageName, long duration, Integer string) {
        DBUtil dbutil = (DBUtil)this.getContext().getBean("dbUtil");
        Object[] params = new Object[]{projectName, pageName, duration, Page.getcurrentTime(), string};
        dbutil.insertRow(this.getJdbcTemplate("automationJdbcTemplate"), DBUtil.getNamedQuery("setLoadTime"), params);
    }

    public void getTestData(Map<String, String> data) throws ParseException {
        DBUtil dbUtil = (DBUtil) this.getContext().getBean("dbUtil");
        Object[] params = new Object[]{Integer.parseInt((String) data.get("TestDataCode")), ((String) data.get("TestDataValues")).toString(), Integer.parseInt((String) data.get("TestDataCode"))};
        dbUtil.insertRow(this.getJdbcTemplate("automationJdbcTemplate"), dbUtil.getNamedQuery("setTestDataProperyList"), params);
        JSONObject testData = (JSONObject) (new JSONParser()).parse(((String) data.get("TestDataValues")).toString());
        data.putAll(testData);
    }
}