package ru.sberbank.ckr.sberboard.increment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EspdMatRepository extends JpaRepository<EspdMat, Long> {
    @Query("SELECT DISTINCT e.subscrId FROM EspdMat e")
    List<String> findAllUniqueSubscribeId();

//    @Query("SELECT e.packageSmd FROM EspdMat e WHERE e.workflowEndDt = MAX(e.workflowEndDt)")
//    String getActualPackageToProcess(String subscrId);

    @Query(value = "SELECT e.package_smd, e.workflow_end_dt\n" +
            "FROM raw_data_increment.espd_mat e\n" +
            "WHERE e.subscr_id = ?1 AND e.espd_status = 'ESPD_OK'\n" +
            "ORDER BY e.workflow_end_dt DESC\n" +
            "LIMIT 1", nativeQuery = true)
    String getActualPackageToProcess(String subscrId);
}
