package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(schema = "raw_data", name = "increment_states")
public class IncrementState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "incr_pack_run_id")
    private Long incrPackRunId;
    @Column(name = "package_smd")
    private String packageSmd;
    @Column(name = "subscr_id")
    private String subscrId;
    @Enumerated(EnumType.STRING)
    @Column(name = "obj_type")
    private IncrementStateObjType objType;
    @Column(name = "objs_in_pack")
    private Integer objsInPack;
    @Column(name = "workflow_end_dt")
    private LocalDateTime workflowEndDt;
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
    private Integer incrementationState;
}

