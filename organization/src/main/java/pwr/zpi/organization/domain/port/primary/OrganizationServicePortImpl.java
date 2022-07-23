package pwr.zpi.organization.domain.port.primary;

import pwr.zpi.organization.application.adpter.primary.OrganizationServiceAdapter;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.domain.model.pub.Organization;
import pwr.zpi.organization.domain.port.mapper.OrganizationMapper;
import pwr.zpi.organization.domain.port.secondary.OrganizationRepositoryPort;

import java.util.UUID;

public class OrganizationServicePortImpl implements OrganizationServiceAdapter {

    private final OrganizationRepositoryPort organizationsRepository;
    private final OrganizationMapper organizationMapper = OrganizationMapper.INSTANCE;

    public OrganizationServicePortImpl(OrganizationRepositoryPort organizationsRepository) {
        this.organizationsRepository = organizationsRepository;
    }

    @Override
    public OrganizationDto createOrganization(OrganizationDto organization) {
        return organizationMapper.OrganizationToOrganizationDto(createOrganizationHelper(organization));
    }

    // TU Lepiej wstrzykiwanie Servisu zrobić, ale idk jak
    public Organization createOrganizationHelper(OrganizationDto organization) {
        var newOrg = Organization.builder()
                .address(organization.getAddress())
                .name(organization.getName())
                .id(UUID.randomUUID()) //Czemu tak? To ustala tu? Czy baza danych zwraca?
                .build();
        OrganizationDto newOrgDto = organizationsRepository.save(organizationMapper
                .OrganizationToOrganizationDto(newOrg));
        organizationsRepository.createOrganization(UUID.fromString((newOrgDto.getId().toString())));
        return organizationMapper.OrganizationDtoToOrganization(newOrgDto);
    }

}
