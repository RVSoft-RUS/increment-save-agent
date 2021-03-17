CREATE TABLE IF NOT EXISTS raw_data.increment_states
(
    package_smd varchar(50),
    subscr_id varchar(50),
    obj_type varchar(20),
    objs_in_pack integer,
    workflow_end_dt timestamp,
    incr_pack_run_id bigint,
    start_dt timestamp,
    end_dt timestamp,
    target_table varchar(50),
    min_ctl_loading bigint,
    max_ctl_loading bigint,
    incrementation_state varchar(20)
);
-- Добавить кому-то права на чтение/изменение?