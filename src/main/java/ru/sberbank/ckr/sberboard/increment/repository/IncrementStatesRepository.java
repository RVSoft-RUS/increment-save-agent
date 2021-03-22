package ru.sberbank.ckr.sberboard.increment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;

@Repository
public interface IncrementStatesRepository extends JpaRepository<IncrementStates, Long> {
    IncrementStates findByPackageSmdAndObjTypeAndIncrPackRunId(String packageSmd, String objType, Long incrPackRunId);
    IncrementStates findByPackageSmdAndObjTypeAndIncrPackRunIdAndTargetTable(String packageSmd, String objType, Long incrPackRunId, String targetTable);
}
