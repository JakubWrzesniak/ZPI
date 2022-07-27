package pwr.zpi.organization.application.adpter.primary;

import org.springframework.stereotype.Component;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;

import java.util.UUID;

@Component
public interface OrganizationServiceAdapter {
    OrganizationDto createOrganization(OrganizationDto organization);
    boolean deleteOrganization(UUID orgId);
}
