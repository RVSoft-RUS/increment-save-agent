package ru.sberbank.ckr.sberboard.increment.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresCtlLoadingDao;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.service.IncrementStateService;
import ru.sberbank.ckr.sberboard.increment.service.PackageService;
import ru.sberbank.ckr.sberboard.increment.service.SaveIncrementService;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.util.List;
import java.util.Map;

@Component
public class CommonQuartzJob implements Job {

//    private static final String manualMode = Utils.getJNDIValue("java:comp/env/increment/manual/mode");

    @Autowired
    private SaveIncrementService saveIncrementService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private JdbcPostgresCtlLoadingDao dao;

    private static final Logger logger = LogManager.getLogger(CommonQuartzJob.class.getSimpleName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//            if (JobManualMode.ON.toString().equals(manualMode)){
//                //TODO Ручной запуск
//            } else {
//                //TODO Автоматический запуск
//            }
        //TODO Журналирование Запуск сервиса применения инкремента
        logger.info("IncrementService starts ");

        Map<EspdMat, List<EspdMatObj>> espdMatObjsForAllActualEspdMat =
                saveIncrementService.getEspdMatObjsForAllActualEspdMat();
        for(Map.Entry<EspdMat, List<EspdMatObj>> entry : espdMatObjsForAllActualEspdMat.entrySet()) {
            packageService.processPackage(entry.getKey(), entry.getValue());
        }
        logger.info("IncrementService finished ");
        //TODO Журналирование Остановка сервиса применения инкремента
    }
}
