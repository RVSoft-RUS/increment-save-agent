package ru.sberbank.ckr.sberboard.increment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Service
public class CrossbarJdbcTemplateBatch {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcPostgresColumnInfoDao jdbcPostgresColumnInfoDao;

    public void upsert(String tableName, HashMap<String, List> dataFromIncrement) {

        List<Column> columnsFromTable = jdbcPostgresColumnInfoDao.getColumnNamesFromTable(tableName);

        HashMap<Column, List> columnDataMap = JoinColumnsAndData(columnsFromTable, dataFromIncrement);

        String sqlQuery = queryBuild(columnDataMap);

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                int pos = 1;
                for (Map.Entry<Column, List> map : columnDataMap.entrySet()) {
                    Array array = preparedStatement.getConnection().createArrayOf(map.getKey().getType(), map.getValue().toArray());
                    preparedStatement.setArray(pos, array);
                    pos++;
                }
            }

            @Override
            public int getBatchSize() {
                return 5;
            }
        });
    }

    private HashMap<Column, List> JoinColumnsAndData(List<Column> columnsFromTable, HashMap<String, List> dataFromIncrement) {
        HashMap<Column, List> columnDataMap = new HashMap<>();
        for (Column column : columnsFromTable) {
            if (dataFromIncrement.containsKey(column.getColumnName())) {
                columnDataMap.put(column, dataFromIncrement.get(column.getColumnName()));
            }
        }
        return columnDataMap;
    }

    private String queryBuild(HashMap<Column, List> columns) {
        String insertColumns = "";
        String unnestColumns = "";
        String primaryKeys = "";
        String updateColumns = "";
        for (Map.Entry<Column, List> map : columns.entrySet()) {
            insertColumns = insertColumns + map.getKey().getColumnName() + ",";
            unnestColumns = unnestColumns + "unnest(?),";

            if (map.getKey().isPrimaryKey()) {
                primaryKeys = primaryKeys + map.getKey().getColumnName() + ",";
            } else
                updateColumns = updateColumns + map.getKey().getColumnName() + " = excluded." + map.getKey().getColumnName() + ",";

        }
        insertColumns = insertColumns.substring(0, insertColumns.length() - 1);
        unnestColumns = unnestColumns.substring(0, unnestColumns.length() - 1);
        primaryKeys = primaryKeys.substring(0, primaryKeys.length() - 1);
        updateColumns = updateColumns.substring(0, updateColumns.length() - 1);
        String sql = "insert into raw_data.test_batch (" + insertColumns + ")\n" +
                "select " + unnestColumns + "\n" +
                "on conflict (" + primaryKeys + ") do update set \n" + updateColumns;
        return sql;
    }
}
