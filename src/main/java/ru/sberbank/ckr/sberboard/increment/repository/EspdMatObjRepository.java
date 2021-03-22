package ru.sberbank.ckr.sberboard.increment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;

import java.util.List;

@Repository
public interface EspdMatObjRepository extends JpaRepository<EspdMatObj, Long> {
    List<EspdMatObj> findAllByPackageSmd(String subscrId);
}
