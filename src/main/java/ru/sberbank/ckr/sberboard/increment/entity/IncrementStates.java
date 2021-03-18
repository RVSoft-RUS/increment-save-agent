package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class IncrementStates {
    private Long id;
    private String packageSmd;
    private String subscrId;
    private String objType;
    private Integer objsInPack;
    private LocalDateTime workflowEndDt;
    private Long incrPackRunId;
    private LocalDateTime startDt;
    private LocalDateTime endDt;
    private String targetTable;
    private Long minCtlLoading;
    private Long maxCtlLoading;
    private String incrementationState;

    public static class IncrementStatesMapper implements RowMapper<IncrementStates> {
        @Override
        public IncrementStates mapRow(ResultSet resultSet, int i) throws SQLException {
            Timestamp timestamp;
            LocalDateTime localDateTime;
            IncrementStates incrementStates = new IncrementStates();
            incrementStates.setId(resultSet.getLong("id"));
            incrementStates.setPackageSmd(resultSet.getString("package_smd"));
            incrementStates.setSubscrId(resultSet.getString("subscr_id"));
            incrementStates.setObjType(resultSet.getString("obj_type"));
            incrementStates.setObjsInPack(resultSet.getInt("objs_in_pack"));

            timestamp = resultSet.getTimestamp("workflow_end_dt");
            localDateTime = timestamp != null ? timestamp.toLocalDateTime() :  null;
            incrementStates.setWorkflowEndDt(localDateTime);

            incrementStates.setIncrPackRunId(resultSet.getLong("incr_pack_run_id"));

            timestamp = resultSet.getTimestamp("start_dt");
            localDateTime = timestamp != null ? timestamp.toLocalDateTime() :  null;
            incrementStates.setStartDt(localDateTime);
            timestamp = resultSet.getTimestamp("end_dt");
            localDateTime = timestamp != null ? timestamp.toLocalDateTime() :  null;
            incrementStates.setEndDt(localDateTime);

            incrementStates.setTargetTable(resultSet.getString("target_table"));
            incrementStates.setMinCtlLoading(resultSet.getLong("min_ctl_loading"));
            incrementStates.setMaxCtlLoading(resultSet.getLong("max_ctl_loading"));
            incrementStates.setIncrementationState(resultSet.getString("incrementation_state"));
            return incrementStates;
        }
    }
}

