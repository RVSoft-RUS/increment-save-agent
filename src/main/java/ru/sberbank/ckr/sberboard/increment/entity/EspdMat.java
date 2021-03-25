package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "raw_data_increment", name = "espd_mat")
//@Table(schema = "raw_data", name = "espd_mat")//Временно //todo Вернуть как было
public class EspdMat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "package_smd")
    String packageSmd;
    @Column(name = "subscr_id")
    String subscrId;
    @Column(name = "package_smd_dt")
    LocalDateTime packageSmdDt;
    @Column(name = "workflow_run_id")
    Long workflowRunId;
    @Column(name = "workflow_name")
    String workflowName;
    @Column(name = "workflow_status")
    String workflowStatus;
    @Column(name = "workflow_start_dt")
    LocalDateTime workflowStartDt;
    @Column(name = "workflow_end_dt")
    LocalDateTime workflowEndDt ;
    @Column(name = "espd_status")
    String espdStatus;
    @Column(name = "espd_err_text")
    String espdErrText;
    @Column(name = "is_partitioned")
    Integer isPartitioned;
    @Column(name = "objs")
    Integer objs;
    @Column(name = "objs_err")
    Integer objsErr;
}
