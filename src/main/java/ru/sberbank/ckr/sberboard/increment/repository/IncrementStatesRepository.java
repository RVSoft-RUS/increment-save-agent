package ru.sberbank.ckr.sberboard.increment.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;

import java.util.List;

@Repository
public interface IncrementStatesRepository extends JpaRepository<IncrementStates, Long> {
    IncrementStates findByPackageSmdAndObjTypeAndIncrPackRunId(String packageSmd, String objType, Long incrPackRunId);
    IncrementStates findByPackageSmdAndObjTypeAndIncrPackRunIdAndTargetTable(String packageSmd, String objType, Long incrPackRunId, String targetTable);
}
