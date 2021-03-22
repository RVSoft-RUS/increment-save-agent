package ru.sberbank.ckr.sberboard.increment.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(schema = "raw_data_increment", name = "espd_mat_obj")
public class EspdMatObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;
    @Column(name = "package_smd")
    String packageSmd;
    @Column(name = "mater_id")
    String materId;
    @Column(name = "status_smd")
    String statusSmd;
    @Column(name = "obj_id")
    Integer objId;
    @Column(name = "mater_tgt_state")
    String materTgtState;
    @Column(name = "mater_tgt_date")
    LocalDateTime materTgtDate;
    @Column(name = "workflow_run_id")
    Long workflowRunId;
    @Column(name = "src_creationdt")
    LocalDateTime srcCreationdt;
    @Column(name = "src_expirationdt")
    LocalDateTime srcExpirationdt;
    @Column(name = "src_plannedstartdt")
    LocalDateTime srcPlannedstartdt;
    @Column(name = "src_real_scheme")
    String srcRealScheme;
    @Column(name = "src_real_table")
    String srcRealTable;
    @Column(name = "mater_tgt_last_error")
    String materTgtLastError;
    @Column(name = "subscr_id")
    String subscrId;
    @Column(name = "to_ignore")
    Integer to_ignore;
}
