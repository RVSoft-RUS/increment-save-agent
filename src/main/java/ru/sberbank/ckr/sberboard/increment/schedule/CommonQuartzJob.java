package ru.sberbank.ckr.sberboard.increment.schedule;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.sberbank.ckr.sberboard.increment.audit.SbBrdServiceAuditService;
import ru.sberbank.ckr.sberboard.increment.audit.SubTypeIdAuditEvent;
import ru.sberbank.ckr.sberboard.increment.audit.SubTypeIdAuditEvent;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresCtlLoadingDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.DataByTableNameRawDataDao;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.OperationsOnTablesRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.service.IncrementStateService;
import ru.sberbank.ckr.sberboard.increment.service.PackageService;
import ru.sberbank.ckr.sberboard.increment.service.SaveIncrementService;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class CommonQuartzJob implements Job {

    private static final String manualMode = Utils.getJNDIValue("java:comp/env/increment/mode/manual");
    private final SaveIncrementService saveIncrementService;
    private final PackageService packageService;
    private final OperationsOnTablesRawDataDAO operationsOnTablesRawDataDAO;
    private final SbBrdServiceAuditService loggerAudit;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        operationsOnTablesRawDataDAO.createTableIfNotExist("cx_txb_log_stat");
        operationsOnTablesRawDataDAO.createTableIfNotExist("test_batch1");
        if (JobManualMode.OFF.toString()
                .equals(manualMode)) {
            loggerAudit.send("Start IncrementService", SubTypeIdAuditEvent.F0.name());

            Map<EspdMat, List<EspdMatObj>> espdMatObjsForAllActualEspdMat =
                    saveIncrementService.getEspdMatObjsForAllActualEspdMat();
            for (Map.Entry<EspdMat, List<EspdMatObj>> entry : espdMatObjsForAllActualEspdMat.entrySet()) {
                packageService.processPackage(entry.getKey(), entry.getValue());
            }
            loggerAudit.send("Finish IncrementService", SubTypeIdAuditEvent.F0.name());
        } else {
            //TODO Ручной запуск
        }
    }
}
