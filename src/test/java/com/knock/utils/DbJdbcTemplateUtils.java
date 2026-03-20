package com.knock.utils;

import lombok.Getter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DbJdbcTemplateUtils {

    @Getter
    private static final JdbcTemplate snowFlakeJdbcTemplate;
    static String env = System.getProperty("db_env");

    static {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("net.snowflake.client.jdbc.SnowflakeDriver");
        dataSource.setUrl(ReadPropertyFile.getProperty(env, "sf_connection_url"));
        dataSource.setUsername(ReadPropertyFile.getProperty(env, "sf_username"));
        dataSource.setPassword(ReadPropertyFile.getProperty(env, "sf_password"));
        snowFlakeJdbcTemplate = new JdbcTemplate(dataSource);
    }
}