package ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspdMatRawDataIncrementDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public List<String> findAllUniqueSubscribeId() {
        return namedParameterJdbcTemplate.queryForList("SELECT DISTINCT e.subscr_id FROM raw_data_increment.espd_mat e", new MapSqlParameterSource(),String.class);
    }


    public EspdMat findActualEspdMatToProcess(String subscrId) {
        String sql = "SELECT*" +
                "FROM raw_data_increment.espd_mat e\n" +
                "WHERE e.subscr_id = :subscrId AND e.espd_status = 'ESPD_OK'\n" +
                "ORDER BY e.workflow_end_dt DESC\n" +
                "LIMIT 1";
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("subscrId", subscrId);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, new EspdMat.EspdMatMapper());
    }


}
