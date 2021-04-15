package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.DataByTableNameRawDataIncrementDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.OperationsOnTablesRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PrepareDataForTransferService {

    private final JdbcPostgresColumnInfoDao jdbcPostgresColumnInfoDao;
    private final OperationsOnTablesRawDataDAO primaryKeyMakerDAO;
    private final SbBrdServiceLoggingService loggerTech;
    private final DataByTableNameRawDataIncrementDao dataByTableNameRawDataDao;

    public List<Column> getColumns(String tableName) {

        final List<String> primaryKeys = getPrimaryKeys(tableName);
        List<Column> columnList = jdbcPostgresColumnInfoDao.getColumnNamesFromTable(tableName);
        columnList.forEach(column -> column.setPrimaryKey(primaryKeys.contains(column.getColumnName())));
        addIncrRunPackIdColumn(columnList);
        return columnList;

    }

    public int findPagesCount(String tableName, int pageSize){
        return dataByTableNameRawDataDao.getCountByTableName(tableName) / pageSize;
    };

    public Page<Map<String, Object>> findPaginated(String tableName, Pageable pageable, IncrementState incrementForCurrentPackage) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        int dataCount = dataByTableNameRawDataDao.getCountByTableName(tableName);
        List<Map<String, Object>> dataList;
        List<String> primaryKeys = getPrimaryKeys(tableName);

        if (dataCount < startItem) {
            dataList = Collections.emptyList();
        } else {
            dataList = dataByTableNameRawDataDao.getDataFromTablePaginated(tableName, primaryKeys, startItem, pageSize, incrementForCurrentPackage);
        }

        return new PageImpl<>(dataList, PageRequest.of(currentPage, pageSize), dataCount);
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
        return primaryKeys;
    }

    void joinColumnsAndData(List<Column> columnList, List<Map<String, Object>> dataList) {
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

    private void addIncrRunPackIdColumn(List<Column> columnList){
        Column incrPacRunIdColumn = new Column();
        incrPacRunIdColumn.setColumnName("INCR_PACK_RUN_ID");
        incrPacRunIdColumn.setType("BIGINT");
        columnList.add(incrPacRunIdColumn);
    }
}
