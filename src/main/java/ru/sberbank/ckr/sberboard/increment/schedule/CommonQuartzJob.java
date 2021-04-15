package ru.sberbank.ckr.sberboard.increment.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import ru.sberbank.ckr.sberboard.increment.audit.SbBrdServiceAuditService;
import ru.sberbank.ckr.sberboard.increment.audit.SubTypeIdAuditEvent;
import ru.sberbank.ckr.sberboard.increment.dao.rawdata.OperationsOnTablesRawDataDAO;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMat;
import ru.sberbank.ckr.sberboard.increment.entity.EspdMatObj;
import ru.sberbank.ckr.sberboard.increment.service.PackageService;
import ru.sberbank.ckr.sberboard.increment.service.SaveIncrementService;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.util.List;
import java.util.Map;

@Component
//@AllArgsConstructor
public class CommonQuartzJob implements Job {

    private static final String manualMode = Utils.getJNDIValue("java:comp/env/increment/mode/manual");
    private static final String manualModePackageSmd = Utils.getJNDIValue("java:comp/env/increment/manualMode/packageSmd");
    private static volatile boolean isRunningOnManualMode = false;

    @Autowired
    private SaveIncrementService saveIncrementService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private OperationsOnTablesRawDataDAO operationsOnTablesRawDataDAO;
    @Autowired
    private SbBrdServiceAuditService loggerAudit;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
                loggerAudit.send("0 packages found for PackageSmd " + manualModePackageSmd + ". " +
                      "IncrementService stopped.", SubTypeIdAuditEvent.F0.name());
            }
            loggerAudit.send("IncrementService <manualMode> stopped.", SubTypeIdAuditEvent.F0.name());
            System.exit(0);
        }
    }
}
