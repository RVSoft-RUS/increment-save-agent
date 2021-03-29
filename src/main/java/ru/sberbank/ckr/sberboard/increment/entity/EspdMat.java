package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EspdMat {

    private String packageSmd;
    private String subscrId;
    private LocalDateTime packageSmdDt;
    private Long workflowRunId;
    private String workflowName;
    private String workflowStatus;
    private LocalDateTime workflowStartDt;
    private LocalDateTime workflowEndDt;
    private String espdStatus;
    private String espdErrText;
    private Integer isPartitioned;
    private Integer objs;
    private Integer objsErr;

    public static class EspdMatMapper implements RowMapper<EspdMat> {
        @Override
        public EspdMat mapRow(ResultSet resultSet, int i) throws SQLException {
            EspdMat espdMat = new EspdMat();
            espdMat.setPackageSmd(resultSet.getString("package_smd"));
            espdMat.setSubscrId(resultSet.getString("subscr_id"));
            espdMat.setPackageSmdDt(resultSet.getTimestamp("package_smd_dt") != null ? resultSet.getTimestamp("package_smd_dt").toLocalDateTime() : null);
            espdMat.setWorkflowRunId(resultSet.getLong("workflow_run_id"));
            espdMat.setWorkflowName(resultSet.getString("workflow_name"));
            espdMat.setWorkflowStatus(resultSet.getString("workflow_status"));
            espdMat.setWorkflowStartDt(resultSet.getTimestamp("workflow_start_dt") != null ? resultSet.getTimestamp("workflow_start_dt").toLocalDateTime() : null);
            espdMat.setWorkflowEndDt(resultSet.getTimestamp("workflow_end_dt") != null ? resultSet.getTimestamp("workflow_end_dt").toLocalDateTime() : null);
            espdMat.setEspdStatus(resultSet.getString("espd_status"));
            espdMat.setEspdErrText(resultSet.getString("espd_err_text"));
            espdMat.setIsPartitioned(resultSet.getInt("is_partitioned"));
            espdMat.setObjs(resultSet.getInt("objs"));
            espdMat.setObjsErr(resultSet.getInt("objs_err"));
            return espdMat;
        }
    }
}








