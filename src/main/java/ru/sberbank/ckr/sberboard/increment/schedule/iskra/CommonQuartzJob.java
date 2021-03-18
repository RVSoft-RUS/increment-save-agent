package ru.sberbank.ckr.sberboard.increment.schedule.iskra;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.sberbank.ckr.sberboard.increment.service.TestService;

@Component
public class CommonQuartzJob implements Job {

    @Autowired
    private TestService testService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
            try {
                testService.test();
            } catch (Exception e) {
                e.printStackTrace();
            }
    }
}
