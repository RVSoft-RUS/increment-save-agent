package ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DataByTableNameRawDataIncrementDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getDataFromTable(String table) {
        String sql = "select * from raw_data_increment." + table;
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> getDataFromTablePaginated(String table, List<String> primaryKeys, int startItem, int pageSize, IncrementState incrementForCurrentPackage) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String keyForOrderBy = String.join(",", primaryKeys);

        parameters.addValue("primaryKeys", keyForOrderBy);
        parameters.addValue("startItem", startItem);
        parameters.addValue("pageSize", pageSize);

        String sql = "SELECT curtable.*, " + incrementForCurrentPackage.getIncrPackRunId() + " as INCR_PACK_RUN_ID" +
                " FROM RAW_DATA_INCREMENT." + table + " as curtable ORDER BY :primaryKeys LIMIT :pageSize OFFSET :startItem";
        return namedParameterJdbcTemplate.queryForList(sql, parameters);
    }

    public Integer getCountByTableName(String table) {
        String sql = "select count(*) from raw_data_increment." + table;
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}
