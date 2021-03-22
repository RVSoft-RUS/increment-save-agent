package ru.sberbank.ckr.sberboard.increment.config.datasource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jndi.JndiTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactoryRawData",
        transactionManagerRef = "transactionManagerRawData",
        basePackages = "ru.sberbank.ckr.sberboard.increment")
@ComponentScan(basePackages = "ru.sberbank.ckr.sberboard.increment")
public class StagingDBConfig {

    private static final Logger logger = LogManager.getLogger("DataConfigurationRawData");

    @Bean(name = "dataSourceRawData")
    public DataSource dataSourceRawData()  {
        DataSource datasource = null;
        try {
            datasource = (DataSource) new JndiTemplate().lookup("java:comp/env/jdbc/Staging");
        } catch (NamingException e) {
            logger.error("Cannot retrieve java:comp/env/jdbc/Staging object!");
            e.printStackTrace();
        }
        try {
            logger.info("----------------------------------STAGING DB INFO----------------------------------");
            logger.info("----------STAGING DB INFO Database: " + datasource.getConnection().getMetaData().getDatabaseProductName() +
                    " " + datasource.getConnection().getMetaData().getDatabaseProductVersion());
            logger.info("----------STAGING DB INFO Schemas info: " + datasource.getConnection().getMetaData().getSchemas());
            logger.info("----------STAGING DB INFO Max connections: " + datasource.getConnection().getMetaData().getMaxConnections());
            logger.info("----------------------------------STAGING DB INFO----------------------------------");
        } catch (SQLException throwables) {
            logger.error("Connection error or info fetching error");
            throwables.printStackTrace();
        }
        return datasource;
    }

    @Bean(name= "entityManagerFactoryRawData")
    public EntityManagerFactory entityManagerFactoryRawData() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setPersistenceUnitName("sberboard_staging");
        em.setDataSource(dataSourceRawData());
        em.setPackagesToScan("ru.sberbank.ckr.sberboard.increment.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(additionalProperties());
        em.afterPropertiesSet();
        return em.getObject();
    }

    private Properties additionalProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.jdbc.fetch_size", "20");
        properties.setProperty("hibernate.connection.release_mode", "after_transaction");
        properties.setProperty("hibernate.connection.isolation", "1");
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");
        properties.setProperty("hibernate.id.new_generator_mappings", "true");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.transaction.jta.platform", "org.springframework.transaction.jta.JtaTransactionManager");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        return properties;
    }

    @Bean(name = "entityManagerRawData")
    public EntityManager entityManagerRawData() {
        return entityManagerFactoryRawData().createEntityManager();
    }

    @Bean(name= "transactionManagerRawData")
    public PlatformTransactionManager transactionManagerRawData(){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactoryRawData());
        return transactionManager;
    }

    @Bean(name = "exceptionTranslationRawData")
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationRawData(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean(name = "jdbcTemplateRawData")
    public JdbcTemplate jdbcTemplateRawData(@Qualifier("dataSourceRawData") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
