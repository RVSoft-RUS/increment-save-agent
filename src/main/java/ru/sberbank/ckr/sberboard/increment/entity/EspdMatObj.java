package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class EspdMatObj {

    private String packageSmd;
    private String materId;
    private String statusSmd;
    private Integer objId;
    private String materTgtState;
    private LocalDateTime materTgtDate;
    private Long workflowRunId;
    private LocalDateTime srcCreationdt;
    private LocalDateTime srcExpirationdt;
    private LocalDateTime srcPlannedstartdt;
    private String srcRealScheme;
    private String srcRealTable;
    private String materTgtLastError;
    private String subscrId;
    private Integer toIgnore;

    public static class EspdMatObjMapper implements RowMapper<EspdMatObj> {
        @Override
        public EspdMatObj mapRow(ResultSet resultSet, int i) throws SQLException {
            EspdMatObj espdMatObj = new EspdMatObj();
            espdMatObj.setPackageSmd(resultSet.getString("package_smd"));
            espdMatObj.setMaterId(resultSet.getString("mater_id"));
            espdMatObj.setStatusSmd(resultSet.getString("status_smd"));
            espdMatObj.setObjId(resultSet.getInt("obj_id"));
            espdMatObj.setMaterTgtState(resultSet.getString("mater_tgt_state"));
            espdMatObj.setMaterTgtDate(resultSet.getTimestamp("mater_tgt_date") != null ? resultSet.getTimestamp("mater_tgt_date").toLocalDateTime() : null);
            espdMatObj.setWorkflowRunId(resultSet.getLong("workflow_run_id"));
            espdMatObj.setSrcCreationdt(resultSet.getTimestamp("src_creationdt") != null ? resultSet.getTimestamp("src_creationdt").toLocalDateTime() : null);
            espdMatObj.setSrcExpirationdt(resultSet.getTimestamp("src_expirationdt") != null ? resultSet.getTimestamp("src_expirationdt").toLocalDateTime() : null);
            espdMatObj.setSrcPlannedstartdt(resultSet.getTimestamp("src_plannedstartdt") != null ? resultSet.getTimestamp("src_plannedstartdt").toLocalDateTime() : null);
            espdMatObj.setSrcRealScheme(resultSet.getString("src_real_scheme"));
            espdMatObj.setSrcRealTable(resultSet.getString("src_real_table"));
            espdMatObj.setMaterTgtLastError(resultSet.getString("mater_tgt_last_error"));
            espdMatObj.setSubscrId(resultSet.getString("subscr_id"));
            espdMatObj.setToIgnore(resultSet.getInt("to_ignore"));

            return espdMatObj;
        }
    }
}
