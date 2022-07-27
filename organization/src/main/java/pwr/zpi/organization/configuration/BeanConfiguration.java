package pwr.zpi.organization.configuration;

import com.pwr.zpi.dataSource.TenantDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pwr.zpi.organization.application.adpter.primary.IssueServiceAdapter;
import pwr.zpi.organization.application.adpter.primary.OrganizationServiceAdapter;
import pwr.zpi.organization.domain.port.primary.IssueServicePort;
import pwr.zpi.organization.domain.port.primary.OrganizationServicePort;
import pwr.zpi.organization.infrastructure.adpter.secondary.IssueRepositoryAdapter;
import pwr.zpi.organization.infrastructure.adpter.secondary.OrganizationRepositoryAdapter;

@Configuration
public class BeanConfiguration {

    @Bean
    OrganizationServiceAdapter organizationServiceAdapter(OrganizationRepositoryAdapter organizationAdapter) {
        return new OrganizationServicePort(organizationAdapter);
    }

    @Bean
    IssueServiceAdapter issueServiceAdapter(IssueRepositoryAdapter issueAdapter) {
        return new IssueServicePort(issueAdapter);
    }

    @Bean
    TenantDataSource tenantDataSource(){
        return TenantDataSource.get();
    }
}