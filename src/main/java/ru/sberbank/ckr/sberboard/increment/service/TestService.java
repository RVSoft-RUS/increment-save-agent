package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStatesRepository;

@RequiredArgsConstructor
@Service
public class TestService {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcPostgresColumnInfoDao dao;
    private final IncrementStatesRepository repository;

    public void test(){
        System.out.println(jdbcTemplate.queryForList("select 'helloworld';", String.class));
        System.out.println(dao.getColumnNamesFromTable("test_batch"));
        System.out.println(repository.findByPackageSmdAndObjTypeAndIncrPackRunId("01", "02", 3L));
    }

}
