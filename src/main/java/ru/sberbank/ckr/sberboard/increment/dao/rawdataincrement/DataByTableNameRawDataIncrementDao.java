package ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataByTableNameRawDataIncrementDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDataFromTable(String table) {
        String sql = "select * from raw_data_increment." + table;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getDataFromTablePaginated(String table, List<String> primaryKeys, int startItem, int pageSize) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String keyForOrderBy = String.join(",", primaryKeys);

        parameters.addValue("primaryKeys",  keyForOrderBy);
        parameters.addValue("startItem",startItem);
        parameters.addValue("pageSize", pageSize);

        String sql = "SELECT * FROM RAW_DATA_INCREMENT." + table + " ORDER BY :primaryKeys LIMIT :pageSize OFFSET :startItem";
        return namedParameterJdbcTemplate.queryForList(sql, parameters);
    }

    public Integer getCountByTableName(String table) {
        String sql = "select count(*) from raw_data_increment." + table;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
