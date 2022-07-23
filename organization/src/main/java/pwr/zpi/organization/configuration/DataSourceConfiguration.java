package pwr.zpi.organization.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class DataSourceConfiguration {

//    @Bean
//    @ConfigurationProperties("multitenancy.master.datasource")
//    public DataSourceProperties masterDataSourceProperties() {
//        DataSourceProperties dataSourceProperties = new DataSourceProperties();
//        return dataSourceProperties;
//    }
//
//    @Bean
//    @LiquibaseDataSource
//    @ConfigurationProperties("multitenancy.master.datasource.hikari")
//    public DataSource masterDataSource() {
//        HikariDataSource dataSource = masterDataSourceProperties()
//                .initializeDataSourceBuilder()
//                .type(HikariDataSource.class)
//                .build();
//        dataSource.setPoolName("masterDataSource");
//        return dataSource;
//    }

}