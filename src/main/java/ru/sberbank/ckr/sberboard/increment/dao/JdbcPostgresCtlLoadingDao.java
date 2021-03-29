package ru.sberbank.ckr.sberboard.increment.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcPostgresCtlLoadingDao {
    private final JdbcTemplate jdbcTemplate;
    private final String ctlLoading = "CTL_LOADING";

    /**
     * Метод возвращает максимальное значение CTL_LOADING (тип Long) для указанной таблицы.
     * Схема для поиска по умолчанию - raw_data_increment.
     *
     * @param table имя таблицы без схемы (схема - raw_data_increment)
     * @return <code>CTL_LOADING</code> (тип {@link java.lang.Long})
     */
    public Long getMaxCtlLoadingFromTable(String table) {
        String sqlSelectMax = "SELECT COALESCE(MAX(?1), 0)  " +
                "FROM ?2";
        return jdbcTemplate.queryForObject(sqlSelectMax, new String[] {ctlLoading, "raw_data." + table}, Long.class);
    }

    /**
     * Метод возвращает минимальное значение CTL_LOADING (тип Long) для указанной таблицы.
     * Схема для поиска по умолчанию - raw_data_increment.
     *
     * @param table имя таблицы без схемы (схема - raw_data_increment)
     * @return <code>CTL_LOADING</code> (тип {@link java.lang.Long})
     */
    public Long getMinCtlLoadingFromTable(String table) {
        String sqlSelectMin = "SELECT COALESCE(MIN(?1), 0) " +
                "FROM ?2" + table;
        return jdbcTemplate.queryForObject(sqlSelectMin, new String[] {ctlLoading, "raw_data." + table}, Long.class);
    }

}
