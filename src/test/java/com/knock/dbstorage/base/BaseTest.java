package com.knock.dbstorage.base;

import FrameWorkPackage.com.rp.automation.framework.util.DBUtil;
import FrameWorkPackage.com.rp.automation.framework.util.ExcelService;
import FrameWorkPackage.com.rp.automation.framework.webdriver.WebDriverBase;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class BaseTest extends WebDriverBase {
    public static final Logger logger = LogManager.getLogger(BaseTest.class);
    static int counter;

    public void updateJdbcTemplateUrl(JdbcTemplate jdbcTemplate, String client) {
        String currentUrl = ((DriverManagerDataSource) jdbcTemplate.getDataSource()).getUrl();
        currentUrl = currentUrl.substring(0, currentUrl.lastIndexOf("/") + 1);
        ((DriverManagerDataSource) jdbcTemplate.getDataSource()).setUrl(currentUrl + client);
    }

    @DataProvider(name = "AMDALoginCredentials") // ,parallel = true)
    public Iterator<Object[]> amdaDataProvider(Method method) {
        logger.info("Sequence counter of amdaDataProvider method===" + counter++);
        return new ExcelService().readTestDataFromExcel(getBeanName("AMDATestDataWorkBookName"), "TestData", method.getName());
    }

    @DataProvider(name = "AMDALoginCredentialsParallel", parallel = true)
    public Iterator<Object[]> amdaDataProviderParallel(Method method) {
        logger.info("Parallel Counter of amdaDataProviderParallel method===" + counter++);
        return new ExcelService().readTestDataFromExcel(getBeanName("AMDATestDataWorkBookName"), "TestData", method.getName());
    }

    @DataProvider(name = "AMDAModuleLoginCredentials") // ,parallel = true)
    public Iterator<Object[]> amdaModuleDataProvider(Method method) {
        logger.info("Sequence counter of amdaModuleDataProvider method===" + counter++);
        String sheetName = "TestData";
        String methodName = method.getName();
        sheetName = methodName.substring(0, methodName.lastIndexOf("_")).toUpperCase();
        String workBookName = getBeanName("AMDATestDataWorkBookName");
        logger.info("amdaModuleDataProvider method work book name -> " + workBookName);
        return new ExcelService().readTestDataFromExcel(workBookName, sheetName, method.getName());
    }

    @DataProvider(name = "AMDAModuleLoginCredentialsParallel", parallel = true)
    public Iterator<Object[]> amdaModuleDataProviderParallel(Method method) {
        logger.info("Parallel Counter of amdaModuleDataProviderParallel method===" + counter++);
        String sheetName = "TestData";
        String methodName = method.getName();
        sheetName = methodName.substring(0, methodName.lastIndexOf("_")).toUpperCase();
        return new ExcelService().readTestDataFromExcel(getBeanName("AMDATestDataWorkBookName"), sheetName, method.getName());
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

    public int getRowCountForQuery(String queryName) {
        DBUtil dbutil = (DBUtil) getContext().getBean("dbUtil");
        JdbcTemplate sfjdbcTemplate = getJdbcTemplate("sfJdbcTemplate");

        //run query for retrieving any records
        List<Map<String, Object>> queryResult = dbutil.getRows(sfjdbcTemplate, DBUtil.getNamedQuery(queryName));
        logger.info("query result: " + queryResult.toString());

        return queryResult.size();
    }

    public <T> T runQuery(JdbcTemplate sfJdbcTemplate, TransactionTemplate transactionTemplate, Supplier<T> supplier) {
        return transactionTemplate.execute(status -> {
            sfJdbcTemplate.execute("ALTER SESSION SET JDBC_QUERY_RESULT_FORMAT='JSON';");
            return supplier.get();
        });
    }
}