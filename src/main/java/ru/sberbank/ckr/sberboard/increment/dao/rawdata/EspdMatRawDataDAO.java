package ru.sberbank.ckr.sberboard.increment.dao.rawdata;

import lombok.RequiredArgsConstructor;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.logging.SbBrdServiceLoggingService;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;


@Service
@RequiredArgsConstructor
public class EspdMatRawDataDAO {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SbBrdServiceLoggingService loggerTech;

    private static final String INSERT_ESPD_MAT = "INSERT INTO raw_data.espd_mat" +
            "(package_smd, subscr_id, package_smd_dt, workflow_run_id, workflow_name," +
            " workflow_status, workflow_start_dt, workflow_end_dt, espd_status, espd_err_text," +
            " is_partitioned, objs, objs_err)" +
            "VALUES(:package_smd, :subscr_id, :package_smd_dt, :workflow_run_id, :workflow_name, " +
            ":workflow_status, :workflow_start_dt, :workflow_end_dt, :espd_status, :espd_err_text," +
            " :is_partitioned, :objs, :objs_err)";


    public void save(EspdMat espdMat) {
        loggerTech.send("Save EspdMat: "+espdMat.toString(), SubTypeIdLoggingEvent.INFO.name());
        MapSqlParameterSource parameters = new MapSqlParameterSource();

        parameters.addValue("package_smd", espdMat.getPackageSmd());
        parameters.addValue("subscr_id", espdMat.getSubscrId());
        parameters.addValue("package_smd_dt", espdMat.getPackageSmdDt());
        parameters.addValue("workflow_run_id", espdMat.getWorkflowRunId());
        parameters.addValue("workflow_name", espdMat.getWorkflowName());
        parameters.addValue("workflow_status", espdMat.getWorkflowStatus());
        parameters.addValue("workflow_start_dt", espdMat.getWorkflowStartDt());
        parameters.addValue("workflow_end_dt", espdMat.getWorkflowEndDt());
        parameters.addValue("espd_status", espdMat.getEspdStatus());
        parameters.addValue("espd_err_text", espdMat.getEspdErrText());
        parameters.addValue("is_partitioned", espdMat.getIsPartitioned());
        parameters.addValue("objs", espdMat.getObjs());
        parameters.addValue("objs_err", espdMat.getObjsErr());

        namedParameterJdbcTemplate.update(INSERT_ESPD_MAT, parameters);

    }
}
