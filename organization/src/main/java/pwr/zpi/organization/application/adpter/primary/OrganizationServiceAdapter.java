package pwr.zpi.organization.application.adpter.primary;

import org.springframework.stereotype.Component;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;

@Component
public interface OrganizationServiceAdapter {
    OrganizationDto createOrganization(OrganizationDto organization);
}
