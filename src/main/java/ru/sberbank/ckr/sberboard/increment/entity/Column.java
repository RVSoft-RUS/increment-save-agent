package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Data
@NoArgsConstructor
public class Column {
    private String columnName;
    private String type;
    private boolean isPrimaryKey;
    private List data;

    public static class ColumnMapper implements RowMapper<Column> {
        @Override
        public Column mapRow(ResultSet resultSet, int i) throws SQLException {
            Column column = new Column();
            column.setColumnName(resultSet.getString("column_name").toUpperCase());
            column.setType(resultSet.getString("udt_name"));
            column.setData(new ArrayList());
            return column;
        }
    }
}