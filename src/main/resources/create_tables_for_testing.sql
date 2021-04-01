CREATE TABLE raw_data_increment.espd_mat (

                                             package_smd varchar(50) NULL,

                                             subscr_id varchar(128) NULL,

                                             package_smd_dt timestamp NULL,

                                             workflow_run_id int8 NULL,

                                             workflow_name varchar(128) NULL,

                                             workflow_status varchar(4000) NULL,

                                             workflow_start_dt timestamp NULL,

                                             workflow_end_dt timestamp NULL,

                                             espd_status varchar(20) NULL,

                                             espd_err_text text NULL,

                                             is_partitioned int4 NULL,

                                             objs int4 NULL,

                                             objs_err int4 NULL

);

CREATE TABLE raw_data_increment.espd_mat_obj (

                                                 package_smd varchar(50) NULL,

                                                 mater_id varchar(50) NULL,

                                                 status_smd varchar(50) NULL,

                                                 obj_id int4 NULL,

                                                 mater_tgt_state varchar(50) NULL,

                                                 mater_tgt_date timestamp NULL,

                                                 workflow_run_id int8 NULL,

                                                 src_creationdt timestamp NULL,

                                                 src_expirationdt timestamp NULL,

                                                 src_plannedstartdt timestamp NULL,

                                                 src_real_scheme varchar(200) NULL,

                                                 src_real_table varchar(200) NULL,

                                                 mater_tgt_last_error varchar(4000) NULL,

                                                 subscr_id varchar(50) NULL,

                                                 to_ignore int4 NULL

);

CREATE TABLE raw_data.espd_mat (

                                   package_smd varchar(50) NULL,

                                   subscr_id varchar(128) NULL,

                                   package_smd_dt timestamp NULL,

                                   workflow_run_id int8 NULL,

                                   workflow_name varchar(128) NULL,

                                   workflow_status varchar(4000) NULL,

                                   workflow_start_dt timestamp NULL,

                                   workflow_end_dt timestamp NULL,

                                   espd_status varchar(20) NULL,

                                   espd_err_text text NULL,

                                   is_partitioned int4 NULL,

                                   objs int4 NULL,

                                   objs_err int4 NULL

);



CREATE TABLE raw_data.espd_mat_obj (

                                       package_smd varchar(50) NULL,

                                       mater_id varchar(50) NULL,

                                       status_smd varchar(50) NULL,

                                       obj_id int4 NULL,

                                       mater_tgt_state varchar(50) NULL,

                                       mater_tgt_date timestamp NULL,

                                       workflow_run_id int8 NULL,

                                       src_creationdt timestamp NULL,

                                       src_expirationdt timestamp NULL,

                                       src_plannedstartdt timestamp NULL,

                                       src_real_scheme varchar(200) NULL,

                                       src_real_table varchar(200) NULL,

                                       mater_tgt_last_error varchar(4000) NULL,

                                       subscr_id varchar(50) NULL,

                                       to_ignore int4 NULL

);


create table raw_data_increment.cx_txb_log_stat (
                                                    PREV_STATUS                                                   varchar(4000)
    ,CONFLICT_ID                                                   varchar(4000)
    ,WAIT                                                          varchar(4000)
    ,LAST_UPD                                                      timestamp(6)
    ,CREATED_BY                                                    varchar(4000)
    ,CURR_STATUS                                                   varchar(4000)
    ,CTL_LOADING                                                   bigint
    ,ROW_ID                                                        varchar(4000)              not null
    ,CTL_VALIDFROM                                                 timestamp(6)
    ,DB_LAST_UPD                                                   timestamp(6)
    ,CREATED                                                       timestamp(6)
    ,ACTION_ID                                                     varchar(4000)
    ,DB_LAST_UPD_SRC                                               varchar(4000)
    ,CTL_CSN                                                       numeric(38,0)
    ,MODIFICATION_NUM                                              numeric(10,0)
    ,CHANGE_DT                                                     timestamp(6)
    ,CTL_ACTION                                                    varchar(4000)
    ,LAST_UPD_BY                                                   varchar(4000)
);


create table raw_data.cx_txb_log_stat (
                                          PREV_STATUS                                                   varchar(4000)
    ,CONFLICT_ID                                                   varchar(4000)
    ,WAIT                                                          varchar(4000)
    ,LAST_UPD                                                      timestamp(6)
    ,CREATED_BY                                                    varchar(4000)
    ,CURR_STATUS                                                   varchar(4000)
    ,CTL_LOADING                                                   bigint
    ,ROW_ID                                                        varchar(4000)              not null
    ,CTL_VALIDFROM                                                 timestamp(6)
    ,DB_LAST_UPD                                                   timestamp(6)
    ,CREATED                                                       timestamp(6)
    ,ACTION_ID                                                     varchar(4000)
    ,DB_LAST_UPD_SRC                                               varchar(4000)
    ,CTL_CSN                                                       numeric(38,0)
    ,MODIFICATION_NUM                                              numeric(10,0)
    ,CHANGE_DT                                                     timestamp(6)
    ,CTL_ACTION                                                    varchar(4000)
    ,LAST_UPD_BY                                                   varchar(4000)
);

create table raw_data_increment.cx_txb_schedule (
                                                    LAST_UPD_BY                                                   varchar(4000)
    ,BREAK_START                                                   numeric(4,0)
    ,BREAK2_END                                                    numeric(4,0)
    ,CTL_ACTION                                                    varchar(4000)
    ,LAST_UPD                                                      timestamp(6)
    ,SHIFT_DATE                                                    timestamp(6)
    ,CONFLICT_ID                                                   varchar(4000)
    ,CREATED_BY                                                    varchar(4000)
    ,SHIFT_NAME                                                    varchar(4000)
    ,CTL_VALIDFROM                                                 timestamp(6)
    ,BREAK_END                                                     numeric(4,0)
    ,SHIFT_END                                                     numeric(4,0)
    ,CREATED                                                       timestamp(6)
    ,CTL_LOADING                                                   bigint
    ,ROW_ID                                                        varchar(4000)              not null
    ,CTL_CSN                                                       numeric(38,0)
    ,DB_LAST_UPD_SRC                                               varchar(4000)
    ,BREAK2_START                                                  numeric(4,0)
    ,DB_LAST_UPD                                                   timestamp(6)
    ,PAR_ROW_ID                                                    varchar(4000)
    ,MODIFICATION_NUM                                              numeric(10,0)
    ,SHIFT_START                                                   numeric(4,0)
);

create table raw_data.cx_txb_schedule (
                                          LAST_UPD_BY                                                   varchar(4000)
    ,BREAK_START                                                   numeric(4,0)
    ,BREAK2_END                                                    numeric(4,0)
    ,CTL_ACTION                                                    varchar(4000)
    ,LAST_UPD                                                      timestamp(6)
    ,SHIFT_DATE                                                    timestamp(6)
    ,CONFLICT_ID                                                   varchar(4000)
    ,CREATED_BY                                                    varchar(4000)
    ,SHIFT_NAME                                                    varchar(4000)
    ,CTL_VALIDFROM                                                 timestamp(6)
    ,BREAK_END                                                     numeric(4,0)
    ,SHIFT_END                                                     numeric(4,0)
    ,CREATED                                                       timestamp(6)
    ,CTL_LOADING                                                   bigint
    ,ROW_ID                                                        varchar(4000)              not null
    ,CTL_CSN                                                       numeric(38,0)
    ,DB_LAST_UPD_SRC                                               varchar(4000)
    ,BREAK2_START                                                  numeric(4,0)
    ,DB_LAST_UPD                                                   timestamp(6)
    ,PAR_ROW_ID                                                    varchar(4000)
    ,MODIFICATION_NUM                                              numeric(10,0)
    ,SHIFT_START                                                   numeric(4,0)
);
CREATE TABLE IF NOT EXISTS raw_data.primary_key_helper
(
    table_name varchar (50) not null
    constraint primary_key_helper_pkey
    primary key,
    p_keys varchar (1000)
);
