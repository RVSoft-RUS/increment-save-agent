package ru.sberbank.ckr.sberboard.increment.schedule.iskra;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import ru.sberbank.ckr.sberboard.increment.common.AutoWiringSpringBeanJobFactory;

@Configuration
@ComponentScan(basePackages = "ru.sberbank.ckr.sberboard")
public class QuartzScheduleConfiguration {

    @Autowired
    private ApplicationContext context;

    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(context);
        return jobFactory;
    }

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(CommonQuartzJob.class)
                .storeDurably()
//                .withIdentity("Job_detail")
//                .withDescription("Job_detail")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
//                .withIdentity("trigger")
//                .withDescription("trigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("*/10 * * ? * *\t")) //	Every 30 seconds
//                .withSchedule(CronScheduleBuilder.cronSchedule("0 */15 * ? * *")) //	Every 15 minutes
                .build();
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobDetail job) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setTriggers(trigger(job));
        schedulerFactoryBean.setJobFactory(springBeanJobFactory());
        schedulerFactoryBean.setJobDetails(jobDetail());
        return schedulerFactoryBean;
    }
}
