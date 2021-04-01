package ru.sberbank.ckr.sberboard.increment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateObjType;

@Repository
public interface IncrementStateRepository extends JpaRepository<IncrementState, Long> {

    IncrementState findByPackageSmdAndObjTypeAndIncrPackRunId(String packageSmd, IncrementStateObjType objType, Long incrPackRunId);
    IncrementState findByPackageSmdAndObjTypeAndIncrPackRunIdAndTargetTable(String packageSmd, String objType, Long incrPackRunId, String targetTable);
}
