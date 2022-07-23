package pwr.zpi.organization.infrastructure.adpter.secondary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.domain.port.secondary.OrganizationRepositoryPort;
import pwr.zpi.organization.infrastructure.adpter.mapper.OrganizationMapper;
import pwr.zpi.organization.infrastructure.datasource.ShardManagementService;
import pwr.zpi.organization.infrastructure.datasource.SqlDatabaseService;

import java.util.UUID;

@Component
public class OrganizationAdapter implements OrganizationRepositoryPort {

    private final OrganizationsRepository organizationRepository;
    private final ShardManagementService shardManagementService;
    private final OrganizationMapper organizationMapper = OrganizationMapper.INSTANCE;

    public OrganizationAdapter(OrganizationsRepository organizationRepository,
                               ShardManagementService shardManagementService) {
        this.organizationRepository = organizationRepository;
        this.shardManagementService = shardManagementService;
    }

    @Override
    public OrganizationDto save(OrganizationDto organizationDto) {
        var organizationEntity = organizationRepository
                .save(organizationMapper.OrganizationDtoToOrganization(organizationDto));
        var organizationSaved = organizationRepository.save(organizationEntity);
        return organizationMapper.OrganizationToOrganizationDto(organizationSaved);
    }

    @Override
    public boolean createOrganization(UUID organizationId) {
        return shardManagementService.createNewOrganization(organizationId);
    }
}
