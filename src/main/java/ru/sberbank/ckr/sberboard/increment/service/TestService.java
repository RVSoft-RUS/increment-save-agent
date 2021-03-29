package ru.sberbank.ckr.sberboard.increment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresColumnInfoDao;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresCtlLoadingDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.EspdMatObjRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.EspdMatRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatObjRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.dao.rawdataincrement.EspdMatRawDataIncrementDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.repository.IncrementStatesRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TestService {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcPostgresColumnInfoDao dao;
    //    private final IncrementStatesRepository repository;
    private final EspdMatObjRawDataDAO espdMatObjRawDataDAO;
    private final EspdMatRawDataDAO espdMatRawDataDAO;
    private final EspdMatObjRawDataIncrementDAO espdMatObjRawDataIncrementDAO;
    private final EspdMatRawDataIncrementDAO espdMatRawDataIncrementDAO;

    public void test() {
        System.out.print(espdMatRawDataIncrementDAO.findAllUniqueSubscribeId().toString());
        EspdMat espdMat = espdMatRawDataIncrementDAO.findActualEspdMatToProcess("e7936129-759e-4431-95d7-76c43ce26b17");
        System.out.println(espdMat);
        List<EspdMatObj> espdMatObjs = espdMatObjRawDataIncrementDAO.findAllByPackageSmd("af50111b-90d2-484d-8fef-dd73e1da9974");
        System.out.print(espdMatObjRawDataIncrementDAO.findAllByPackageSmd("af50111b-90d2-484d-8fef-dd73e1da9974").toString());
        espdMatRawDataDAO.save(espdMat);
        for (EspdMatObj espdMatObj : espdMatObjs) {
            espdMatObjRawDataDAO.save(espdMatObj);
        }
    }
}
