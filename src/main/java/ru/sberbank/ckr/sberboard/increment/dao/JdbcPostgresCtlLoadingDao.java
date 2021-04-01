package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcPostgresCtlLoadingDao {
    private final JdbcTemplate jdbcTemplate;

    /**
     * Метод возвращает максимальное значение CTL_LOADING (тип Long) для указанной таблицы.
     * Схема для поиска по умолчанию - raw_data_increment.
     *
     * @param table имя таблицы без схемы (схема - raw_data_increment)
     * @return <code>CTL_LOADING</code> (тип {@link java.lang.Long})
     */
    public Long getMaxCtlLoadingFromTable(String table) {
        String sqlSelectMax = "SELECT MAX(CTL_LOADING) " +
                "FROM raw_data_increment." + table;
        return jdbcTemplate.queryForObject(sqlSelectMax, Long.class);
    }

    /**
     * Метод возвращает минимальное значение CTL_LOADING (тип Long) для указанной таблицы.
     * Схема для поиска по умолчанию - raw_data_increment.
     *
     * @param table имя таблицы без схемы (схема - raw_data_increment)
     * @return <code>CTL_LOADING</code> (тип {@link java.lang.Long})
     */
    public Long getMinCtlLoadingFromTable(String table) {
        String sqlSelectMin = "SELECT MIN(CTL_LOADING) " +
                "FROM raw_data_increment." + table;
        return jdbcTemplate.queryForObject(sqlSelectMin, Long.class);
    }
}
