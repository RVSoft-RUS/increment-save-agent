package ru.sberbank.ckr.sberboard.increment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;

@Repository
public interface EspdMatRepository extends JpaRepository<EspdMat, Long> {
    EspdMat findByPackageSmdAndSubscrId(String packageSmd, String subscrId);
}
