package pwr.zpi.organization.domain.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import pwr.zpi.organization.domain.model.dto.IssueDto;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;
import pwr.zpi.organization.domain.model.org.Issue;
import pwr.zpi.organization.domain.model.pub.Organization;
import pwr.zpi.organization.domain.port.secondary.IssueRepositoryPort;
import pwr.zpi.organization.domain.port.secondary.OrganizationRepositoryPort;
import pwr.zpi.organization.domain.port.mapper.IssueMapper;
import pwr.zpi.organization.domain.port.mapper.OrganizationMapper;

import java.util.UUID;

@Log4j2
@AllArgsConstructor
public class OrganizationServiceImpl {




//    void addTables(){
//        var standardServiceRegistry = new StandardServiceRegistryBuilder().applySettings(dbSettings).build();
//        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);
//        metadataSources.addAnnotatedClass(Organization.class);
//        Metadata metadata = metadataSources.buildMetadata();
//
//        SchemaExport schemaExport = new SchemaExport();
//        schemaExport.setFormat(true);
//        schemaExport.setOutputFile("create.sql");
//        schemaExport.createOnly(EnumSet.of(TargetType.SCRIPT), metadata);
//    }

}
