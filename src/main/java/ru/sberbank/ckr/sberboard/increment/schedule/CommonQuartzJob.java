package ru.sberbank.ckr.sberboard.increment.schedule;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sberbank.ckr.sberboard.increment.service.TestService;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

@Component
public class CommonQuartzJob implements Job {

    private static final String manualMode = Utils.getJNDIValue("java:comp/env/increment/manual/mode");

    @Autowired
    private TestService testService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            if (JobManualMode.ON.toString().equals(manualMode)){
                //TODO Ручной запуск
            } else {
                //TODO Автоматический запуск
            }
    }
}
