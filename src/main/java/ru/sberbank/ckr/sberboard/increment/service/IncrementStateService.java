package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementState;
import ru.sberbank.ckr.sberboard.increment.entity.enums.IncrementStateStatus;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStateRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class IncrementStateService {

    private final IncrementStateRepository repository;

    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "transactionManagerRawData")
    public IncrementState saveNewIncrementStates(IncrementState incrementState) {
        return repository.save(incrementState);
    }

    void updateIncrementState(IncrementState incrementState) {
        repository.save(incrementState);
    }

}
