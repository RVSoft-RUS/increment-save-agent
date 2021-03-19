package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.entity.IncrementStates;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStatesRepository;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TestService {

    private final JdbcTemplate jdbcTemplate;
    private final IncrementStatesRepository repository;

    public void test(){
        System.out.println(jdbcTemplate.queryForList("select 'helloworld';", String.class));
        IncrementStates incrementStates =
                repository.findByPackageSmdAndObjTypeAndIncrPackRunIdAndTargetTable("01", "02", 3L, "04");
        //IncrementStates incrementStates2 = repository.("01", "02", 3L, "04");
        incrementStates.setSubscrId("ЯЯЯЯЯЯЯ");
        incrementStates.setIncrementationState("ФФФФФФФ");
        incrementStates.setStartDt(LocalDateTime.now());
        repository.save(incrementStates);

        incrementStates = new IncrementStates();
        incrementStates.setSubscrId("000");
        repository.save(incrementStates);
    }
}
