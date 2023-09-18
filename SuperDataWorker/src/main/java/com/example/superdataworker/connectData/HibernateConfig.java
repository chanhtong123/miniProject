package com.example.superdataworker.connectData;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class HibernateConfig {
    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
        sessionFactoryBean.setPackagesToScan("com.example.superdataworker.model");

        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "update"); // Chỉ dùng trong môi trường phát triển
        hibernateProperties.setProperty("hibernate.show_sql", "true");

        sessionFactoryBean.setHibernateProperties(hibernateProperties);

        sessionFactoryBean.setDataSource(dataSource()); // Đảm bảo bạn đã cấu hình DataSource

        return sessionFactoryBean;
    }

    @Bean
    public DataSource dataSource() {
        // Cấu hình DataSource, ví dụ: HikariCP
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/SuperDataWorker");
        config.setUsername("root");
        config.setPassword("12345");
        return new HikariDataSource(config);
    }

}
