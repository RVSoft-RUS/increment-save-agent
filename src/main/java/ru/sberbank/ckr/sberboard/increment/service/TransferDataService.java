package ru.sberbank.ckr.sberboard.increment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresUpsertDao;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.util.*;

@Service
public class TransferDataService {


    @Autowired
    JdbcPostgresUpsertDao jdbcPostgresUpsertDao;

    public void upsert(String tableName, List<Column> dataFromIncrement) {

        String sqlQuery = queryBuild(dataFromIncrement, tableName);

        jdbcPostgresUpsertDao.upset(sqlQuery, dataFromIncrement);

    }

    private String queryBuild(List<Column> columns, String table) {
        String insertColumns = "";
        String unnestColumns = "";
        String primaryKeys = "";
        String updateColumns = "";
        for (Column column : columns) {
            insertColumns = insertColumns + column.getColumnName() + ",";
            unnestColumns = unnestColumns + "unnest(?),";

            if (column.isPrimaryKey()) {
                primaryKeys = primaryKeys + column.getColumnName() + ",";
            } else
                updateColumns = updateColumns + column.getColumnName() + " = excluded." + column.getColumnName() + ",";

        }
        insertColumns = insertColumns.substring(0, insertColumns.length() - 1);
        unnestColumns = unnestColumns.substring(0, unnestColumns.length() - 1);
        primaryKeys = primaryKeys.substring(0, primaryKeys.length() - 1);
        updateColumns = updateColumns.substring(0, updateColumns.length() - 1);
        String sql = "insert into raw_data."+table+"(" + insertColumns + ")\n" +
                "select " + unnestColumns + "\n" +
                "on conflict (" + primaryKeys + ") do update set \n" + updateColumns;
        return sql;
    }
}
