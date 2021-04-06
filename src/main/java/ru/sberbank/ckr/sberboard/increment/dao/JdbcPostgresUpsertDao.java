package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.sql.Array;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcPostgresUpsertDao {

    private final JdbcTemplate jdbcTemplate;

    private static final Logger logger = LogManager.getLogger(JdbcPostgresUpsertDao.class.getSimpleName());

    public void upsert(String sqlQuery, List<Column> columns) {
        logger.info("Executing UPSERT: " + sqlQuery);
        jdbcTemplate.update(sqlQuery, preparedStatement -> {
            for (int i = 0; i < columns.size(); i++) {
                int position = i + 1;
                Array array = preparedStatement.getConnection().createArrayOf(columns.get(i).getType(), columns.get(i).getData().toArray());
                preparedStatement.setArray(position, array);
            }
        });
    }
}
