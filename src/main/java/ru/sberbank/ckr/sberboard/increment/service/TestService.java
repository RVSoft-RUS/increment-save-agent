package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresCtlLoadingDao;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStatesRepository;

@RequiredArgsConstructor
@Service
public class TestService {

//    private final JdbcTemplate jdbcTemplate;
//    private final JdbcPostgresColumnInfoDao dao;
//    private final IncrementStatesRepository repository;
//    private final SaveIncrementService saveIncrementService;
    private final JdbcPostgresCtlLoadingDao ctlLoadingDao;

    public void test(){
//        System.out.println(jdbcTemplate.queryForList("select 'helloworld';", String.class));
//        System.out.println(dao.getColumnNamesFromTable("test_batch"));
//        System.out.println(repository.findByPackageSmdAndObjTypeAndIncrPackRunId("01", "02", 3L));

        //List<String> list = saveIncrementService.getUniqueDescriptions();
        //System.out.println(list);
//        for (String s: list) {
//            String a = saveIncrementService.getActualPackageToProcess(s);
//            System.out.println(a);
//        }
       // list.forEach(id -> System.out.println(saveIncrementService.getActualPackageToProcess(id)));
//        System.out.println(saveIncrementService.getEspdMatObjsForAllActualEspdMat());
        System.out.println(ctlLoadingDao.getMaxCtlLoadingFromTable("espd_mat"));
    }

}
