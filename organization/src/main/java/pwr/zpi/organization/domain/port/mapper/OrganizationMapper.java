package pwr.zpi.organization.domain.port.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.domain.model.pub.Organization;

import java.util.UUID;

@Mapper
public interface OrganizationMapper {

    OrganizationMapper INSTANCE = Mappers.getMapper( OrganizationMapper.class );

    @Mapping(source = "id", target = "id", qualifiedByName = "UUIDToSting")
    OrganizationDto OrganizationToOrganizationDto(Organization organization);

    @Mapping(source = "id", target = "id", qualifiedByName = "StringToUUID")
    Organization OrganizationDtoToOrganization(OrganizationDto organization);

    @Named("UUIDToSting")
    static String UUIDToSting(UUID uuid) {
        return uuid.toString();
    }

    @Named("StringToUUID")
    static UUID StringToUUID(String id) {
        return UUID.fromString(id);
    }
}
