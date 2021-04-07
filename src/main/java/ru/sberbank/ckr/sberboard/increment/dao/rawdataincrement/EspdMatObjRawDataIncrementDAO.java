package ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspdMatObjRawDataIncrementDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SbBrdServiceLoggingService loggerTech;

    public List<EspdMatObj> findAllByPackageSmd(String packageSmd) {
        loggerTech.send("Search all tables by packageSmd: "+packageSmd, SubTypeIdLoggingEvent.INFO.name());
        String sql = "select*from raw_data_increment.espd_mat_obj where package_smd = :packageSmd";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("packageSmd", packageSmd);

        return namedParameterJdbcTemplate.query(sql, parameters, new EspdMatObj.EspdMatObjMapper());
    }

}
