package ru.sberbank.ckr.sberboard.increment.dao.rawdata;


import lombok.RequiredArgsConstructor;
import org.codehaus.commons.nullanalysis.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PrimaryKeyMakerDAO {
    private final JdbcTemplate jdbcTemplate;

    private final SbBrdServiceLoggingService loggerTech;

    /**
     *
     * @param tableName имя таблицы в схеме raw_data
     * @param columns Список столбцов таблицы, для которых необходимо сделать констрэйнт Primary Key
     *                (составной, если в списке более 1 столбца)
     */
    @Transactional("transactionManagerRawData")
    public void createPrimaryKeysOnTable(@NotNull String tableName, @NotNull List<String> columns) {
        loggerTech.send("Create primary keys: "+columns.toString()+" for table "+tableName, SubTypeIdLoggingEvent.INFO.name());
        StringBuilder columnsStr = new StringBuilder();
        columns.forEach(column -> columnsStr.append(column).append(","));
        columnsStr.deleteCharAt(columnsStr.length() - 1);

        String sql = "ALTER TABLE raw_data." + tableName +
                " ADD CONSTRAINT pk_auto_" + tableName + " PRIMARY KEY ( " +
                columnsStr.toString() + " )";
        jdbcTemplate.execute(sql);

    }

}
