package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JdbcPostgresColumnInfoDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Метод возвращает список List<Column> с информацией по каждой колонке. Схема для поиска по умолчанию - raw_data_increment.
     * @return ArrayList<Column>
     * @param table имя таблицы без схемы
     */
    public List<Column> getColumnNamesFromTable(String table) {
        String sqlFindPrimaryKeys = "SELECT a.attname\n" +
                "FROM   pg_index i\n" +
                "JOIN   pg_attribute a ON a.attrelid = i.indrelid\n" +
                "AND a.attnum = ANY(i.indkey)\n" +
                "WHERE  i.indrelid = 'raw_data." + table + "'::regclass\n" +
                "AND    i.indisprimary;";
        List<String> primaryKeys = jdbcTemplate.queryForList(sqlFindPrimaryKeys, String.class);

        String sqlGetColumns = "select * \n" +
                "from information_schema.columns\n" +
                "where table_schema = 'raw_data_increment' AND table_name = '";
        List<Column> result =
                jdbcTemplate.query(sqlGetColumns + table + "'", new Column.ColumnMapper());

        result.forEach(column -> column.setPrimaryKey(primaryKeys.contains(column.getColumnName())));
        return result;
    }

    public List<Map<String,Object>> getDataFromTable(String table) {
        String sql = "select*from raw_data_increment."+table;
        return jdbcTemplate.queryForList(sql);
    }
}
