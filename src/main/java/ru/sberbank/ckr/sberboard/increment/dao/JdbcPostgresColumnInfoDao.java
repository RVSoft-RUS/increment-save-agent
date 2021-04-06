package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JdbcPostgresColumnInfoDao {
    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LogManager.getLogger(JdbcPostgresColumnInfoDao.class.getSimpleName());

    public String getPrimaryKeysFromHelper(String table) {
        logger.info("Get primary key from raw_data.primary_key_helper");
        String sql = "SELECT UPPER(p_keys) FROM raw_data.primary_key_helper " +
                "WHERE UPPER(table_name) = UPPER(?)";
        return jdbcTemplate.queryForObject(sql, new String[]{table}, String.class);
    }

    public List<String> getPrimaryKeys(String table) {
        String sqlFindPrimaryKeys = "SELECT UPPER(a.attname)\n" +
                "FROM   pg_index i\n" +
                "JOIN   pg_attribute a ON a.attrelid = i.indrelid\n" +
                "AND a.attnum = ANY(i.indkey)\n" +
                "WHERE  i.indrelid = UPPER('raw_data." + table + "')::regclass\n" +
                "AND    i.indisprimary;";

        return jdbcTemplate.queryForList(sqlFindPrimaryKeys, String.class);
    }

    public List<Column> getColumnNamesFromTable(String table) {
        logger.info("Get column names from table "+table);

        String sqlGetColumns = "select * \n" +
                "from information_schema.columns\n" +
                "where table_schema = 'raw_data_increment' AND UPPER(table_name) = UPPER(?)";

        return jdbcTemplate.query(sqlGetColumns, new String[]{table}, new Column.ColumnMapper());

    }

    public List<Map<String, Object>> getDataFromTable(String table) {
        logger.info("Get data from table "+table);
        String sql = "select*from raw_data_increment." + table;
        return jdbcTemplate.queryForList(sql);
    }
}
