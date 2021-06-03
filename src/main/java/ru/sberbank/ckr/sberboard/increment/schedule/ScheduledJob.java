package ru.sberbank.ckr.sberboard.increment.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.audit.SbBrdServiceAuditService;
import ru.sberbank.ckr.sberboard.increment.audit.SubTypeIdAuditEvent;
import ru.sberbank.ckr.sberboard.increment.dao.JdbcPostgresTablesInfoDao;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.logging.SubTypeIdLoggingEvent;
import ru.sberbank.ckr.sberboard.increment.service.PackageService;
import ru.sberbank.ckr.sberboard.increment.service.SaveIncrementService;
import ru.sberbank.ckr.sberboard.increment.service.TableService;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ScheduledJob {

    private static final String manualMode = Utils.getJNDIValue("java:comp/env/increment/mode/manual");
    private static final String manualModePackageSmd = Utils.getJNDIValue("java:comp/env/increment/manualMode/packageSmd");
    private static volatile boolean isRunningOnManualMode = false;

    private final SaveIncrementService saveIncrementService;
    private final PackageService packageService;
    private final SbBrdServiceAuditService loggerAudit;
    private final ApplicationContext context;
    private final JdbcPostgresTablesInfoDao jdbcPostgresTablesInfoDao;
    private final TableService tableService;

    @Scheduled(fixedDelay = 15 * 60 * 1000)
    public void execute() {
//
//        long MA = 1024;
//        String tableName = "s_srv_req";
//        System.out.println(jdbcPostgresTablesInfoDao.getTableSize(tableName));
//        System.out.println(jdbcPostgresTablesInfoDao.getTableSizeAsString(tableName));
//        List<String> tables = jdbcPostgresTablesInfoDao.getAllTablesFromRawData();
//
//        for (String table : tables) {
//            int pageSize = tableService.getPageSize(table);
//            System.out.println(table + ": " + pageSize);
//        }
//

        if (JobManualMode.OFF.toString().equals(manualMode)) {
            loggerAudit.send("Start IncrementService", SubTypeIdAuditEvent.F0.name());

            Map<EspdMat, List<EspdMatObj>> espdMatObjsForAllActualEspdMat =
                    saveIncrementService.getEspdMatObjsForAllActualEspdMat();
            for (Map.Entry<EspdMat, List<EspdMatObj>> entry : espdMatObjsForAllActualEspdMat.entrySet()) {
                try {
                    packageService.processPackage(entry.getKey(), entry.getValue());
                } catch (Exception e) {
                    loggerAudit.send(e.getMessage(), SubTypeIdLoggingEvent.INFO.name());
                }
            }
            loggerAudit.send("Finish IncrementService", SubTypeIdAuditEvent.F0.name());
        } else {
            if (isRunningOnManualMode) {
                return;
            }
            isRunningOnManualMode = true;
            loggerAudit.send("Start IncrementService in manual mode. " +
                    "PackageSmd for processing: " + manualModePackageSmd, SubTypeIdAuditEvent.F0.name());
            try {
                final EspdMat espdMatForManual = saveIncrementService.getEspdMatByPackageSmd(manualModePackageSmd);
                final List<EspdMatObj> espdMatObjsByEspdMat = saveIncrementService.getEspdMatObjsByEspdMat(espdMatForManual);
                packageService.processPackage(espdMatForManual, espdMatObjsByEspdMat);
            } catch (EmptyResultDataAccessException e) {
                loggerAudit.send("0 packages found for PackageSmd " + manualModePackageSmd + ". ", SubTypeIdAuditEvent.F0.name());
            } catch (Throwable t) {
                loggerAudit.send("Unknown exception: " + t.getMessage(), SubTypeIdAuditEvent.F0.name());
            }
            loggerAudit.send("IncrementService <manualMode> stopped.", SubTypeIdAuditEvent.F0.name());
            ((ConfigurableApplicationContext) context).close();
        }
    }

}
