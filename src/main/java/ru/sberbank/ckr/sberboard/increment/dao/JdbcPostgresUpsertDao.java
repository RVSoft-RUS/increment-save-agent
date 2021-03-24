package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.Column;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JdbcPostgresUpsertDao {

    private final JdbcTemplate jdbcTemplate;

    public void upset(String sqlQuery, List<Column> columns ){
        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int j) throws SQLException {

                for (int i = 0 ; i < columns.size(); i ++) {
                    int position = i+1;
                    Array array = preparedStatement.getConnection().createArrayOf(columns.get(i).getType(), columns.get(i).getData().toArray());
                    preparedStatement.setArray(position, array);
                }
            }

            @Override
            public int getBatchSize() {
                return 5;
            }
        });
    }


}