package pl.skrys.configuration;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories(basePackages = {"pl.skrys.dao"})
@EnableTransactionManagement
public class HibernatePersistenceConfig {

    //Hibernate configuration
    @Bean
    public DataSource getDataSource(){
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/managRob?characterEncoding=utf-8");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        System.out.println("getDataSource");
        return dataSource;
    }

    private Properties getHibernateProperties(){
        Properties properties = new Properties();
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.hbm2ddl.auto", "update");//todo properties.put("hibernate.hbm2ddl.auto", "create"); lub "update"
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.default_schema", "public");
        System.out.println("getHibernateProperties");
        return properties;
    }


    @Bean
    public EntityManager entityManager() {
        return entityManagerFactory().getObject().createEntityManager();
    }

    //JPA
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(getDataSource());
        factoryBean.setPackagesToScan("pl.skrys.app");


        factoryBean.setPackagesToScan("pl.skrys.app");


        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL95Dialect");
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJpaProperties(getHibernateProperties());
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager (EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
        return new PersistenceExceptionTranslationPostProcessor();
    }

}

