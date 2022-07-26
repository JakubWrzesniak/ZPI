package pwr.zpi.organization.infrastructure.adpter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.infrastructure.entity.pub.Organization;



@Mapper
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper( OrganizationMapper.class );

    OrganizationDto OrganizationToOrganizationDto(Organization organization);

    Organization OrganizationDtoToOrganization(OrganizationDto organization);
}
