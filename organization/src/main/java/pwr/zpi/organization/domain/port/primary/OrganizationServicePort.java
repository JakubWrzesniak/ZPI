package pwr.zpi.organization.domain.port.primary;

import pwr.zpi.organization.application.adpter.primary.OrganizationServiceAdapter;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.domain.model.pub.Organization;
import pwr.zpi.organization.domain.port.mapper.OrganizationMapper;
import pwr.zpi.organization.domain.port.secondary.OrganizationRepositoryPort;

import java.util.UUID;

public class OrganizationServicePort implements OrganizationServiceAdapter {

    private final OrganizationRepositoryPort organizationsRepository;
    private final OrganizationMapper organizationMapper = OrganizationMapper.INSTANCE;

    public OrganizationServicePort(OrganizationRepositoryPort organizationsRepository) {
        this.organizationsRepository = organizationsRepository;
    }

    @Override
    public OrganizationDto createOrganization(OrganizationDto organization) {
        return organizationMapper.OrganizationToOrganizationDto(createOrganizationHelper(organization));
    }

    @Override
    public boolean deleteOrganization(UUID orgId) {
        organizationsRepository.deleteOrganization(orgId);
        return organizationsRepository.delete(orgId);
    }

    public Organization createOrganizationHelper(OrganizationDto organization) {
        // Tu tylko mapper zamiast buildera
        var newOrg = Organization.builder()
                .address(organization.getAddress())
                .name(organization.getName())
                .id(UUID.randomUUID())
                .build();
        OrganizationDto newOrgDto = organizationsRepository.save(organizationMapper
                .OrganizationToOrganizationDto(newOrg));
        organizationsRepository.createOrganization(newOrgDto.getId());
        return organizationMapper.OrganizationDtoToOrganization(newOrgDto);
    }

}
