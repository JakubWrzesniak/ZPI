package pwr.zpi.organization.infrastructure.adpter.secondary;

import com.pwr.zpi.dataSource.TenantDataSource;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.domain.port.secondary.OrganizationRepositoryPort;
import pwr.zpi.organization.infrastructure.adpter.mapper.OrganizationMapper;

import java.util.UUID;

@Component
@Log4j2
public class OrganizationRepositoryAdapter implements OrganizationRepositoryPort {

    private final OrganizationsRepository organizationRepository;
    private final TenantDataSource tenantDataSource;
    private final OrganizationMapper organizationMapper = OrganizationMapper.INSTANCE;

    public OrganizationRepositoryAdapter(OrganizationsRepository organizationRepository,
                                         TenantDataSource tenantDataSource) {
        this.organizationRepository = organizationRepository;
        this.tenantDataSource = tenantDataSource;
    }

    @Override
    public OrganizationDto save(OrganizationDto organizationDto) {
        var organizationEntity = organizationMapper.OrganizationDtoToOrganization(organizationDto);
        var organizationSaved = organizationRepository.save(organizationEntity);
        return organizationMapper.OrganizationToOrganizationDto(organizationSaved);
    }

    @Override
    public boolean delete(UUID orgId) {
        var organization = organizationRepository.findByIdEquals(orgId);
        if(organization.isPresent()){
            organizationRepository.delete(organization.get());
            log.info(String.format("organization %s removed", organization.get().getName()));
        }
        log.warn(String.format("Cannot get organization with id %s", orgId));
        return true;
    }

    @Override
    public void createOrganization(UUID organizationId) {
        tenantDataSource.createOrganizationDatabase(organizationId);
    }

    @Override
    public void deleteOrganization(UUID organizationId) {
        tenantDataSource.dropOrganizationDatabase(organizationId);
    }
}
