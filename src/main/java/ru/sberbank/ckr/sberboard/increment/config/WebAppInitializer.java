package ru.sberbank.ckr.sberboard.increment.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import ru.sberbank.ckr.sberboard.increment.config.logging.Log4j2ConfigurationFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.ServiceLoader;

public class WebAppInitializer implements WebApplicationInitializer {

    private static ServiceLoader<Log4j2ConfigurationFactory> codecSetLoader
            = ServiceLoader.load(Log4j2ConfigurationFactory.class);

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.scan("ru.sberbank.ckr.sberboard.increment");
        container.addListener(new ContextLoaderListener(context));

        ServletRegistration.Dynamic dispatcher =
                container.addServlet("sberboard-increment", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}