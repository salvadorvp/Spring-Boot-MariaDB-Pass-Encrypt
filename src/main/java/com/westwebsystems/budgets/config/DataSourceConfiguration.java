package com.westwebsystems.budgets.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.westwebsystems.budgets.helputils.SecurityUtils;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:database.properties")
public class DataSourceConfiguration {

    @Value("${database.url}")
    private String dbUrl;

    @Value("${database.username}")
    private String dbUsername;

    @Value("${database.password}")
    private String dbPassword;

    @Value("${database.driver-class-name}")
    private String dbDriverClass;

    @Value("${security.key-file}")
    private String keyFile;

    private String clearedPassword = "";

    @Bean
    public DataSource dataSource()  throws GeneralSecurityException, IOException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        // Expecting something like "oracle.jdbc.OracleDriver"
        dataSource.setDriverClassName(dbDriverClass);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        clearedPassword = SecurityUtils.decrypt(dbPassword, new File(keyFile));
        dataSource.setPassword(clearedPassword);
        return dataSource;
    }
}