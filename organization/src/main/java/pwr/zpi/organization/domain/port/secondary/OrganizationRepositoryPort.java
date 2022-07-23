package pwr.zpi.organization.domain.port.secondary;

import pwr.zpi.organization.domain.model.dto.OrganizationDto;

import java.util.UUID;

public interface OrganizationRepositoryPort {
    OrganizationDto save(OrganizationDto organization);
    boolean createOrganization(UUID organizationId);
}
