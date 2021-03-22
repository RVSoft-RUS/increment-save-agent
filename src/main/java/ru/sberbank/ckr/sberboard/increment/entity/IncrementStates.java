package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "raw_data", name = "increment_states")
public class IncrementStates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column (name = "package_smd")
    private String packageSmd;
    @Column (name = "subscr_id")
    private String subscrId;
    @Column(name = "obj_type")
    private String objType;
    @Column (name = "objs_in_pack")
    private Integer objsInPack;
    @Column (name = "workflow_end_dt")
    private LocalDateTime workflowEndDt;
    @Column (name = "incr_pack_run_id")
    private Long incrPackRunId;
    @Column (name = "start_dt")
    private LocalDateTime startDt;
    @Column (name = "end_dt")
    private LocalDateTime endDt;
    @Column (name = "target_table")
    private String targetTable;
    @Column(name = "min_ctl_loading")
    private Long minCtlLoading;
    @Column(name = "max_ctl_loading")
    private Long maxCtlLoading;
    @Column (name = "incrementation_state")
    private String incrementationState;
}

