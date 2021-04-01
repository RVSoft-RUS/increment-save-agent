package ru.sberbank.ckr.sberboard.increment.event.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.event.PackageProcessedEvent;
import ru.sberbank.ckr.sberboard.increment.event.TableProcessedEvent;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStateRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class TableProcessedEventListener {

    private final IncrementStateRepository repository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "transactionManagerRawData")
    public void processTableProcessedEventCommited(TableProcessedEvent event){
        IncrementState incrementForCurrentTable = event.getIncrementState();
        incrementForCurrentTable.setIncrementationState(IncrementStateStatus.TABLE_PROCESSED_SUCCESSFULLY.status);
        incrementForCurrentTable.setEndDt(LocalDateTime.now());
        repository.save(incrementForCurrentTable);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "transactionManagerRawData")
    public void processTableProcessedEventRolledBack(TableProcessedEvent event){
        IncrementState incrementForCurrentTable = event.getIncrementState();
        incrementForCurrentTable.setIncrementationState(IncrementStateStatus.TABLE_PROCESS_FAILED_ROLLED_BACK.status);
        incrementForCurrentTable.setEndDt(LocalDateTime.now());
        repository.save(incrementForCurrentTable);
    }

}
