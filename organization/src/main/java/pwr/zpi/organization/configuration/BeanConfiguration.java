package pwr.zpi.organization.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pwr.zpi.organization.application.adpter.primary.IssueServiceAdapter;
import pwr.zpi.organization.application.adpter.primary.OrganizationServiceAdapter;
import pwr.zpi.organization.domain.port.primary.IssueServicePortImpl;
import pwr.zpi.organization.domain.port.primary.OrganizationServicePortImpl;
import pwr.zpi.organization.infrastructure.adpter.secondary.IssueAdapter;
import pwr.zpi.organization.infrastructure.adpter.secondary.OrganizationAdapter;

@Configuration
public class BeanConfiguration {

    @Bean
    OrganizationServiceAdapter organizationServiceAdapter(OrganizationAdapter organizationAdapter) {
        return new OrganizationServicePortImpl(organizationAdapter);
    }

    @Bean
    IssueServiceAdapter issueServiceAdapter(IssueAdapter issueAdapter) {
        return new IssueServicePortImpl(issueAdapter);
    }
}