package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class PrepareDataForTransferService {

    private final JdbcPostgresColumnInfoDao jdbcPostgresColumnInfoDao;
    private final TransferDataService transferDataService;

    public void getDataTable(String tableName) {
        List<Column> columnList = jdbcPostgresColumnInfoDao.getColumnNamesFromTable(tableName);
        List<Map<String, Object>> dataList = jdbcPostgresColumnInfoDao.getDataFromTable(tableName);

        joinColumnsAndData(columnList, dataList);

        transferDataService.upsert(tableName, columnList);
    }

    private void joinColumnsAndData(List<Column> columnList, List<Map<String, Object>> dataList) {
        for (Column column : columnList) {
            for (Map<String, Object> mapData : dataList) {
                for (Map.Entry<String, Object> pair : mapData.entrySet()) {
                    if (column.getColumnName().equals(pair.getKey())) {
                        column.getData().add(pair.getValue());
                    }
                }
            }
        }
    }
}
