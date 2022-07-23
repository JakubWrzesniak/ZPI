package pwr.zpi.organization.configuration;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import pwr.zpi.organization.configuration.tenant.TenantIdentifierResolver;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    @Bean
    public DataSource dataSource() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setUser("sa");
        dataSource.setPassword("Stud@1234");
        dataSource.setEncrypt(true);
        dataSource.setTrustServerCertificate(true);
        dataSource.setServerName("localhost");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfBean = new LocalContainerEntityManagerFactoryBean();
        emfBean.setPackagesToScan("pwr.zpi.organization.infrastructure.entity");
        emfBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfBean.setDataSource(dataSource());
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put(Environment.MULTI_TENANT,
                MultiTenancyStrategy.DATABASE);
        jpaProperties.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER,
                new SchemaMultiTenantConnectionProvider());
        jpaProperties.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER,
                new TenantIdentifierResolver());
        jpaProperties.put(Environment.DIALECT,
                "org.hibernate.dialect.SQLServerDialect");
        jpaProperties.put(Environment.DRIVER,
                "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        jpaProperties.put(Environment.HBM2DDL_AUTO, "none");
//        jpaProperties.put(Environment.JAKARTA_HBM2DDL_SCRIPTS_ACTION, "create");
//        jpaProperties.put(Environment.JAKARTA_HBM2DDL_SCRIPTS_CREATE_TARGET, "create.sql");
//        jpaProperties.put(Environment.JAKARTA_HBM2DDL_CREATE_SOURCE, "metadata");
        emfBean.setJpaPropertyMap(jpaProperties);
        return emfBean;
    }
}