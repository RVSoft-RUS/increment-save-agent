package ru.sberbank.ckr.sberboard.increment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class IncrementStatesRepository{
    private final JdbcTemplate jdbcTemplate;

    @Transactional(transactionManager = "transactionManagerRawData")
    public void add(IncrementStates incrementStates) {
        jdbcTemplate.update("INSERT INTO raw_data.increment_states " +
            "(package_smd,\n" +
                "    subscr_id,\n" +
                "    obj_type,\n" +
                "    objs_in_pack,\n" +
                "    workflow_end_dt,\n" +
                "    incr_pack_run_id,\n" +
                "    start_dt,\n" +
                "    end_dt,\n" +
                "    target_table,\n" +
                "    min_ctl_loading,\n" +
                "    max_ctl_loading,\n" +
                "    incrementation_state) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                incrementStates.getPackageSmd(),
                incrementStates.getSubscrId(),
                incrementStates.getObjType(),
                incrementStates.getObjsInPack(),
                incrementStates.getWorkflowEndDt(),
                incrementStates.getIncrPackRunId(),
                incrementStates.getStartDt(),
                incrementStates.getEndDt(),
                incrementStates.getTargetTable(),
                incrementStates.getMinCtlLoading(),
                incrementStates.getMaxCtlLoading(),
                incrementStates.getIncrementationState());
    }

    @Transactional(transactionManager = "transactionManagerRawData")
    public void update(IncrementStates incrementStates) {
        jdbcTemplate.update("UPDATE raw_data.increment_states SET " +
            "package_smd = ?," +
                "    subscr_id = ?,\n" +
                "    obj_type = ?,\n" +
                "    objs_in_pack = ?,\n" +
                "    workflow_end_dt = ?,\n" +
                "    incr_pack_run_id = ?,\n" +
                "    start_dt = ?,\n" +
                "    end_dt = ?,\n" +
                "    target_table = ?,\n" +
                "    min_ctl_loading = ?,\n" +
                "    max_ctl_loading = ?,\n" +
                "    incrementation_state = ? " +
                        "WHERE id = ?",
                incrementStates.getPackageSmd(),
                incrementStates.getSubscrId(),
                incrementStates.getObjType(),
                incrementStates.getObjsInPack(),
                incrementStates.getWorkflowEndDt(),
                incrementStates.getIncrPackRunId(),
                incrementStates.getStartDt(),
                incrementStates.getEndDt(),
                incrementStates.getTargetTable(),
                incrementStates.getMinCtlLoading(),
                incrementStates.getMaxCtlLoading(),
                incrementStates.getIncrementationState(),
                incrementStates.getId());
    }

    @Transactional(transactionManager = "transactionManagerRawData", readOnly = true)
    public IncrementStates find(String packageSmd, String objType, long incrPackRunId) {
        List<IncrementStates> incrementStatesList = jdbcTemplate.query("SELECT * FROM raw_data.increment_states " +
                        "WHERE package_smd = ? AND obj_type = ? AND incr_pack_run_id = ?",
                        new Object[]{packageSmd, objType, incrPackRunId}, new IncrementStates.IncrementStatesMapper());
        if (incrementStatesList.isEmpty()) return null;
        return incrementStatesList.get(0);
    }
}
