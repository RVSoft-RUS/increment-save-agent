package ru.sberbank.ckr.sberboard.increment.schedule;

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
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

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
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        String cronValue = Utils.getJNDIValue("java:comp/env/increment/cronExpression");
        return TriggerBuilder.newTrigger().forJob(job)
                .withSchedule(CronScheduleBuilder.cronSchedule(cronValue))
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
