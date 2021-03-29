CREATE OR REPLACE VIEW raw_data_increment.ESPD_READY_TO_RECEIVE AS
SELECT CASE(alls - good) WHEN  0 THEN 1 ELSE 0 END AS READY
FROM (
         SELECT count(*) AS alls, COALESCE(sum(incrementation_state) / 100, 0)  AS good
         FROM raw_data.increment_states i INNER JOIN raw_data_increment.espd_mat e
                                                     ON (i.package_smd = e.package_smd)
         WHERE espd_status = 'ESPD_OK') as w
-- должна выполнять логику сравнения пакетов в espd_mat и  increment_states определять,
-- что все пакеты загруженные и отмеченные в espd_mat атрибутом "ESPD_OK",
-- успешно обработаны агентом обновления данных и имеют соответствующий статус
-- обработки пакета = "100" в increment_states. В таком случае вьюха должна
-- возвращать значение 1 иначе 0.
