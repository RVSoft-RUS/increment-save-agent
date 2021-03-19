package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "raw_data", name = "increment_states")
public class IncrementStates {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "package_smd")
    private String packageSmd;
    @Column(name = "subscr_id")
    private String subscrId;
    @Column(name = "obj_type")
    private String objType;
    @Column(name = "objs_in_pack")
    private Integer objsInPack;
    @Column(name = "workflow_end_dt")
    private LocalDateTime workflowEndDt;
    @Column(name = "incr_pack_run_id")
    private Long incrPackRunId;
    @Column(name = "start_dt")
    private LocalDateTime startDt;
    @Column(name = "end_dt")
    private LocalDateTime endDt;
    @Column(name = "target_table")
    private String targetTable;
    @Column(name = "min_ctl_loading")
    private Long minCtlLoading;
    @Column(name = "max_ctl_loading")
    private Long maxCtlLoading;
    @Column(name = "incrementation_state")
    private String incrementationState;

//    public static class IncrementStatesMapper implements RowMapper<IncrementStates> {
//        @Override
//        public IncrementStates mapRow(ResultSet resultSet, int i) throws SQLException {
//            Timestamp timestamp;
//            LocalDateTime localDateTime;
//            IncrementStates incrementStates = new IncrementStates();
//            incrementStates.setId(resultSet.getLong("id"));
//            incrementStates.setPackageSmd(resultSet.getString("package_smd"));
//            incrementStates.setSubscrId(resultSet.getString("subscr_id"));
//            incrementStates.setObjType(resultSet.getString("obj_type"));
//            incrementStates.setObjsInPack(resultSet.getInt("objs_in_pack"));
//
//            timestamp = resultSet.getTimestamp("workflow_end_dt");
//            localDateTime = timestamp != null ? timestamp.toLocalDateTime() :  null;
//            incrementStates.setWorkflowEndDt(localDateTime);
//
//            incrementStates.setIncrPackRunId(resultSet.getLong("incr_pack_run_id"));
//
//            timestamp = resultSet.getTimestamp("start_dt");
//            localDateTime = timestamp != null ? timestamp.toLocalDateTime() :  null;
//            incrementStates.setStartDt(localDateTime);
//            timestamp = resultSet.getTimestamp("end_dt");
//            localDateTime = timestamp != null ? timestamp.toLocalDateTime() :  null;
//            incrementStates.setEndDt(localDateTime);
//
//            incrementStates.setTargetTable(resultSet.getString("target_table"));
//            incrementStates.setMinCtlLoading(resultSet.getLong("min_ctl_loading"));
//            incrementStates.setMaxCtlLoading(resultSet.getLong("max_ctl_loading"));
//            incrementStates.setIncrementationState(resultSet.getString("incrementation_state"));
//            return incrementStates;
//        }
//    }
}

