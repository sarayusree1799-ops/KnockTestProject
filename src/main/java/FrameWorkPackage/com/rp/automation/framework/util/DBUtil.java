package FrameWorkPackage.com.rp.automation.framework.util;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component("dbUtil")
public class DBUtil {
    private static final Properties queries = new Properties();

    public static String getNamedQuery(String queryName) {
        return queries.getProperty(queryName);
    }

    public Object getObject(JdbcTemplate jdbcTemplate, String query) {
        return jdbcTemplate.queryForObject(query, Object.class);
    }

    public Object getObject(JdbcTemplate jdbcTemplate, String query, Object[] params) {
        return jdbcTemplate.queryForObject(query, params, Object.class);
    }

    public List<Map<String, Object>> getRows(JdbcTemplate jdbcTemplate, String query, Object[] params) {
        return jdbcTemplate.queryForList(query, params);
    }

    public List<Map<String, Object>> getRows(JdbcTemplate jdbcTemplate, String query, Object[] params, int[] argTypes) {
        return jdbcTemplate.queryForList(query, params, argTypes);
    }

    public List<Map<String, Object>> getRows(JdbcTemplate jdbcTemplate, String query) {
        return jdbcTemplate.queryForList(query);
    }

    public List<TempBean> getRowsUsingRowMapper(JdbcTemplate jdbcTemplate, String query, RowMapper<TempBean> rowMapper) {
        return jdbcTemplate.query(query, rowMapper);
    }

    public int setObject(JdbcTemplate jdbcTemplate, String query, Object[] params) {
        return jdbcTemplate.update(query, params);
    }

    public int setObject(JdbcTemplate jdbcTemplate, String query) {
        return jdbcTemplate.update(query);
    }

    public void createProcedure(JdbcTemplate jdbcTemplate, String query) {
        jdbcTemplate.execute(query);
    }

    public int update(JdbcTemplate jdbcTemplate, String query) {
        return jdbcTemplate.update(query);
    }

    public int update(JdbcTemplate jdbcTemplate, String query, Object[] params) {
        return jdbcTemplate.update(query, params);
    }

    public int update(JdbcTemplate jdbcTemplate, String query, Object[] params, int[] argTypes) {
        return jdbcTemplate.update(query, params, argTypes);
    }

    public void insertRow(JdbcTemplate jdbcTemplate, String query, Object[] params) {
        jdbcTemplate.update(query, params);
    }

    static {
        try {
            File file = new File(System.getProperty("user.dir") + "/src/test/resources/queries");
            File[] files = file.listFiles((File d, String name) -> name.endsWith(".xml"));

            for (File f : files) {
                queries.loadFromXML(DBUtil.class.getResourceAsStream("/queries/" + f.getName()));
            }

        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
