package com.realpage.com.Reports.com.rp.reports.utils;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
    private static int mapperCount = 1;
    private static Map<String, Object> attributes = new HashMap<>();
    private static Map<String, Object> suiteNameMapper = new HashMap<>();
    private static Map<String, AuthorDetails> classLevelAuthors = new HashMap<>();

    public static final String SUITE_CLASS_NAME_MAPPER = "atuSuiteNameMapper";

    public Attributes() {
    }

    public static Object getAttribute(String paramString) {
        return attributes.get(paramString);
    }

    public static void setAttribute(String paramString, Object paramObject) {
        attributes.put(paramString, paramObject);
    }

    public static void makeNull() {
        attributes = null;
    }

    public static String getSuiteNameMapper(String paramString) {
        return (String) suiteNameMapper.get(paramString);
    }

    public static Map<String, Object> getSuiteNameMapperMap() {
        return suiteNameMapper;
    }

    public static void setSuiteNameMapper(String paramString) {
        suiteNameMapper.put(paramString, "atuSuiteNameMapper" + mapperCount++);
    }

    public static void setClassLevelAuthors(String paramstring, AuthorDetails paramAuthorDetails) {
        classLevelAuthors.put(paramstring, paramAuthorDetails);
    }

    public static AuthorDetails getClassLevelAuthors(String paramString) {
        return (AuthorDetails) classLevelAuthors.get(paramString);
    }
}