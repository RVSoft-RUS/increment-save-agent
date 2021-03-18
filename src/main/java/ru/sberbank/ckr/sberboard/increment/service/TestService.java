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
        IncrementStates incrementStates = repository.find("01", "02", 3L);
        incrementStates.setSubscrId("SSSSSSSSSSSSSS");
        incrementStates.setIncrementationState("---+++++---");
        incrementStates.setStartDt(LocalDateTime.now());
        repository.update(incrementStates);
    }

}
