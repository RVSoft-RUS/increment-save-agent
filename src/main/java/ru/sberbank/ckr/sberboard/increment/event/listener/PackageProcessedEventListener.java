package ru.sberbank.ckr.sberboard.increment.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.event.PackageProcessedEvent;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStateRepository;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PackageProcessedEventListener {

    private final IncrementStateRepository repository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "transactionManagerRawData")
    public void processPackageProcessedEventCommited(PackageProcessedEvent event){
        IncrementState incrementForCurrentPackage = event.getIncrementState();
        incrementForCurrentPackage.setIncrementationState(IncrementStateStatus.PACKAGE_PROCESSED_SUCCESSFULLY.status);
        incrementForCurrentPackage.setEndDt(LocalDateTime.now());
        repository.save(incrementForCurrentPackage);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "transactionManagerRawData")
    public void processPackageProcessedEventRolledBack(PackageProcessedEvent event){
        IncrementState incrementForCurrentPackage = event.getIncrementState();
        incrementForCurrentPackage.setIncrementationState(IncrementStateStatus.PACKAGE_ROLLED_BACK.status);
        incrementForCurrentPackage.setEndDt(LocalDateTime.now());
        repository.save(incrementForCurrentPackage);
    }

}
