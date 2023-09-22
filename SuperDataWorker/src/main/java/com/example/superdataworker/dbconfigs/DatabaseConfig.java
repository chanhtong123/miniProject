package com.example.superdataworker.dbconfigs;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configurable
@EnableJpaRepositories(basePackages = "com.example.superdataworker.repositories")
@EntityScan(basePackages = "com.example.superdataworker.models")
public class DatabaseConfig {
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan("com.example.superdataworker.models");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        return em;
    }
}
