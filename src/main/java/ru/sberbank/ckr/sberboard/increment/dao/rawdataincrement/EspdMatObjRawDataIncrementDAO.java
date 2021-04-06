package ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EspdMatObjRawDataIncrementDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static final Logger logger = LogManager.getLogger(EspdMatRawDataIncrementDAO.class.getSimpleName());

    public List<EspdMatObj> findAllByPackageSmd(String packageSmd) {
        logger.info("Search all tables by packageSmd: "+packageSmd);
        String sql = "select*from raw_data_increment.espd_mat_obj where package_smd = :packageSmd";

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("packageSmd", packageSmd);

        return namedParameterJdbcTemplate.query(sql, parameters, new EspdMatObj.EspdMatObjMapper());
    }

}
