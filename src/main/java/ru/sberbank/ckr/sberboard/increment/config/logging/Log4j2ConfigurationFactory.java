package ru.sberbank.ckr.sberboard.increment.config.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.LoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import ru.sberbank.ckr.sberboard.increment.utils.Utils;

import java.net.URI;

@Plugin(name = "Log4j2ConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class Log4j2ConfigurationFactory extends ConfigurationFactory {

    private static final String filePath = Utils.getJNDIValue("java:comp/env/logs/filePath");
    private static final String arhSize = Utils.getJNDIValue("java:comp/env/logs/arhSize");


    public Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.add(setConsoleAppender(builder));
        builder.add(setSberboardAppender(builder));
        builder.add(setRootLogger(builder));
        builder.add(setSberboardLogger(builder));
        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, "SberboardServiceLogBuilder", null);
    }


    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[] {"*"};
    }


    public RootLoggerComponentBuilder setRootLogger(ConfigurationBuilder<BuiltConfiguration> builder){
        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.INFO);
        rootLogger.add(builder.newAppenderRef("stdout"));
        rootLogger.add(builder.newAppenderRef("sberboard-service"));
        return rootLogger;
    }

    public AppenderComponentBuilder setConsoleAppender(ConfigurationBuilder<BuiltConfiguration> builder){
        return builder.newAppender("stdout", "Console")
                .addAttribute("target",ConsoleAppender.Target.SYSTEM_OUT).add(setConsoleLayout(builder));

    }

    public LayoutComponentBuilder setConsoleLayout(ConfigurationBuilder<BuiltConfiguration> builder){
        return builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n%throwable");
    }



    public ComponentBuilder setSberboardTriggerPolicy(ConfigurationBuilder<BuiltConfiguration> builder){
        return builder.newComponent("Policies")
                .addComponent(builder.newComponent("CronTriggeringPolicy").addAttribute("schedule", "0 0 0 * * ?"))
                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy").addAttribute("size", arhSize));
    }

    public LayoutComponentBuilder setSberboardLayout(ConfigurationBuilder<BuiltConfiguration> builder){
        return builder.newLayout("PatternLayout")
                .addAttribute("pattern", "%d [%t] %-5level: %msg%n");
    }

    public AppenderComponentBuilder setSberboardAppender(ConfigurationBuilder<BuiltConfiguration> builder){
        return builder.newAppender("sberboard-service", "RollingFile")
                .addAttribute("fileName", filePath + "/sberboard-service.log")
                .addAttribute("filePattern", filePath + "/archive/sberboard-service-%d{MM-dd-yy}.log.gz")
                .add(setSberboardLayout(builder))
                .addComponent(setSberboardTriggerPolicy(builder));
    }

    public LoggerComponentBuilder setSberboardLogger(ConfigurationBuilder<BuiltConfiguration> builder){
        LoggerComponentBuilder logger = builder.newLogger("ru.sbrf.ckr.sberboard", Level.INFO);
        logger.add(builder.newAppenderRef("sberboard-service"));
        logger.addAttribute("additivity", false);
        return logger;
    }
}
