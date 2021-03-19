package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class JdbcPostgresColumnInfoService {
    private final JdbcTemplate jdbcTemplate;

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
                "where table_name = '";
        List<Column> result =
                jdbcTemplate.query(sqlGetColumns + table + "'", new Column.ColumnMapper());

        result.forEach(column -> column.setPrimaryKey(primaryKeys.contains(column.getColumnName())));
        return result;
    }
}
