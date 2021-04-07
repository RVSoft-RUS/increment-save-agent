package ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspdMatRawDataIncrementDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SbBrdServiceLoggingService loggerTech;

    public List<String> findAllUniqueSubscribeId() {
        loggerTech.send("Search all unique packages", SubTypeIdLoggingEvent.INFO.name());
        return namedParameterJdbcTemplate.queryForList("SELECT DISTINCT e.subscr_id FROM raw_data_increment.espd_mat e", new MapSqlParameterSource(),String.class);
    }


    public EspdMat findActualEspdMatToProcess(String subscrId) {
        loggerTech.send("Search actual packages to process by subscribe id: "+subscrId, SubTypeIdLoggingEvent.INFO.name());
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
