package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.PrimaryKeyMakerDAO;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PrepareDataForTransferService {

    private final JdbcPostgresColumnInfoDao jdbcPostgresColumnInfoDao;
    private final PrimaryKeyMakerDAO primaryKeyMakerDAO;

    private final SbBrdServiceLoggingService loggerTech;

    public List<Column> getDataTable(String tableName) {

        final List<String> primaryKeys = getPrimaryKeys(tableName);

        List<Column> columnList = jdbcPostgresColumnInfoDao.getColumnNamesFromTable(tableName);

        columnList.forEach(column -> column.setPrimaryKey(primaryKeys.contains(column.getColumnName())));

        List<Map<String, Object>> dataList = jdbcPostgresColumnInfoDao.getDataFromTable(tableName);

        joinColumnsAndData(columnList, dataList);

        return columnList;

//        transferDataService.upsert(tableName, columnList);
    }

    private List<String> getPrimaryKeys(String tableName) {
        List<String> primaryKeys = jdbcPostgresColumnInfoDao.getPrimaryKeys(tableName);
        if (primaryKeys.size() == 0) {
            loggerTech.send(tableName + " doesn't have primary key", SubTypeIdLoggingEvent.INFO.name());
            primaryKeys = Arrays
                    .stream(jdbcPostgresColumnInfoDao.getPrimaryKeysFromHelper(tableName)
                            .split("\\+")).map(String::trim)
                    .collect(Collectors.toList());
            primaryKeyMakerDAO.createPrimaryKeysOnTable(tableName, primaryKeys);
        }
        loggerTech.send("Table " + tableName + " has primary keys " + primaryKeys.toString(), SubTypeIdLoggingEvent.INFO.name());
        return primaryKeys;
    }

    private void joinColumnsAndData(List<Column> columnList, List<Map<String, Object>> dataList) {
        loggerTech.send("Join columns and data", SubTypeIdLoggingEvent.INFO.name());
        final Map<String, List> colNames = columnList.stream().collect(Collectors.
                toMap(column -> column.getColumnName(), column -> new ArrayList<>()));

        dataList.stream().forEach(mapData -> mapData.entrySet().stream()
                .forEach(pair -> {
                    if (colNames.containsKey(pair.getKey().toUpperCase()))
                        colNames.get(pair.getKey().toUpperCase()).add(pair.getValue());
                }));

        columnList.stream().forEach(column -> {
            if (colNames.containsKey(column.getColumnName()))
                column.setData(colNames.get(column.getColumnName()));
        });
    }
}
