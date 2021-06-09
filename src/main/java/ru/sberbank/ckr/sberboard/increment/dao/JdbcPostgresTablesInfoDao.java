package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcPostgresTablesInfoDao {
    private final JdbcTemplate jdbcTemplate;
    private final SbBrdServiceLoggingService loggerTech;

    public String getPrimaryKeysFromHelper(String table) {
        loggerTech.send("Get primary key from raw_data.primary_key_helper", SubTypeIdLoggingEvent.INFO.name());
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
        loggerTech.send("Get column names from table " + table, SubTypeIdLoggingEvent.INFO.name());

        String sqlGetColumns = "select * \n" +
                "from information_schema.columns\n" +
                "where table_schema = 'raw_data_increment' AND UPPER(table_name) = UPPER(?)";

        return jdbcTemplate.query(sqlGetColumns, new String[]{table}, new Column.ColumnMapper());
    }

    /**
     *
     * @param tableName имя таблицы <b>в схеме raw_data_increment</b>
     * @return Количество полей в таблице (0 при несуществующей таблице)
     */
    public Integer getColumnsCountFromTable(String tableName) {
        String sqlgetColumnsCountFromTable = "select count(*) from information_schema.columns\n" +
                "where table_schema = 'raw_data_increment' AND table_name = '" + tableName.toLowerCase() + "'";

        return jdbcTemplate.queryForObject(sqlgetColumnsCountFromTable, Integer.class);
    }

    /**
     * @param tableName имя таблицы <b>в схеме raw_data_increment</b>
     * @return String Строка с единицами измерения KB, MB, GB или TB. Для несуществующей
     * таблицы возвращает 0.
     */
    public String getTableSizeAsString(String tableName) {
        String sqlGetTableSize = "SELECT pg_size_pretty(cast(" + getTableSize(tableName) + " as BIGINT))";
        return jdbcTemplate.queryForObject(sqlGetTableSize, String.class);
    }

    /**
     * @param tableName имя таблицы <b>в схеме raw_data</b>
     * @return Long Размер таблицы в байтах с учетом индексов и т. д. Для несуществующей
     * таблицы возвращает 0.
     */
    public Long getTableSize(String tableName) {
        try {
            String sqlGetTableSize = "SELECT pg_total_relation_size('raw_data_increment." + tableName + "')";
            return jdbcTemplate.queryForObject(sqlGetTableSize, Long.class);
        } catch (Exception e) {
            return 0L;
        }
    }


    //Для дебага метод
    public List<String> getAllTablesFromRawData() {
        String sqlGetColumns = "select table_name " +
                "from information_schema.tables " +
                "where table_schema = 'raw_data'";

        return jdbcTemplate.queryForList(sqlGetColumns, String.class);
    }

}
