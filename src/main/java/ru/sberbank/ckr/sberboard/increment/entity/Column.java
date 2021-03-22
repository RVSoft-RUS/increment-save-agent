package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
public class Column {
    private String columnName;
    private String type;
    private boolean isPrimaryKey;

    public static class ColumnMapper implements RowMapper<Column> {
        @Override
        public Column mapRow(ResultSet resultSet, int i) throws SQLException {
            Column column = new Column();
            column.setColumnName(resultSet.getString("column_name"));
            column.setType(resultSet.getString("udt_name"));
            return column;
        }
    }
}