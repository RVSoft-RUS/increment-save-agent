package ru.sberbank.ckr.sberboard.increment.dao.rawdata;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;


@Service
@RequiredArgsConstructor
public class EspdMatObjRawDataDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SbBrdServiceLoggingService loggerTech;


    private static final String insertEspdMatObj = "INSERT INTO raw_data.espd_mat_obj " +
            "(package_smd, mater_id, status_smd, obj_id, mater_tgt_state, mater_tgt_date," +
            " workflow_run_id, src_creationdt, src_expirationdt, src_plannedstartdt," +
            " src_real_scheme, src_real_table, mater_tgt_last_error, subscr_id, to_ignore)" +
            " VALUES (:package_smd, :mater_id, :status_smd, :obj_id, :mater_tgt_state, :mater_tgt_date" +
            ", :workflow_run_id, :src_creationdt, :src_expirationdt, :src_plannedstartdt" +
            ", :src_real_scheme, :src_real_table, :mater_tgt_last_error, :subscr_id, :to_ignore)";


    public void save(EspdMatObj espdMatObj) {
        loggerTech.send("Save EspdMatObj: "+espdMatObj.toString(), SubTypeIdLoggingEvent.INFO.name());
        MapSqlParameterSource parameters = new MapSqlParameterSource();


        parameters.addValue("package_smd", espdMatObj.getPackageSmd());
        parameters.addValue("mater_id", espdMatObj.getMaterId());
        parameters.addValue("status_smd", espdMatObj.getStatusSmd());
        parameters.addValue("obj_id", espdMatObj.getObjId());
        parameters.addValue("mater_tgt_state", espdMatObj.getMaterTgtState());
        parameters.addValue("mater_tgt_date", espdMatObj.getMaterTgtDate());
        parameters.addValue("workflow_run_id", espdMatObj.getWorkflowRunId());
        parameters.addValue("src_creationdt", espdMatObj.getSrcCreationdt());
        parameters.addValue("src_expirationdt", espdMatObj.getSrcExpirationdt());
        parameters.addValue("src_plannedstartdt", espdMatObj.getSrcPlannedstartdt());
        parameters.addValue("src_real_scheme", espdMatObj.getSrcRealScheme());
        parameters.addValue("src_real_table", espdMatObj.getSrcRealTable());
        parameters.addValue("mater_tgt_last_error", espdMatObj.getMaterTgtLastError());
        parameters.addValue("subscr_id", espdMatObj.getSubscrId());
        parameters.addValue("to_ignore", espdMatObj.getToIgnore());

        namedParameterJdbcTemplate.update(insertEspdMatObj, parameters);

    }
}
