package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestService {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcPostgresColumnInfoService service;

    public void test(){
        System.out.println(jdbcTemplate.queryForList("select 'helloworld';", String.class));

        System.out.println(service.getColumnNamesFromTable("test"));
    }

}
