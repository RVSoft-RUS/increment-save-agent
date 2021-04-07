package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.sql.Array;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcPostgresUpsertDao {

    private final JdbcTemplate jdbcTemplate;

    private final SbBrdServiceLoggingService loggerTech;

    public void upsert(String sqlQuery, List<Column> columns) {
        loggerTech.send("Executing UPSERT: " + sqlQuery, SubTypeIdLoggingEvent.INFO.name());
        jdbcTemplate.update(sqlQuery, preparedStatement -> {
            for (int i = 0; i < columns.size(); i++) {
                int position = i + 1;
                Array array = preparedStatement.getConnection().createArrayOf(columns.get(i).getType(), columns.get(i).getData().toArray());
                preparedStatement.setArray(position, array);
            }
        });
    }
}
