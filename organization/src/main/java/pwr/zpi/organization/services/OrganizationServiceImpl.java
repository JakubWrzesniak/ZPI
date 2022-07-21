package pwr.zpi.organization.services;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import pwr.zpi.organization.dto.OrganizationDto;
import pwr.zpi.organization.tenant.TenantContext;
import pwr.zpi.organization.entity.org.Issue;
import pwr.zpi.organization.repository.IssueRepository;
import pwr.zpi.organization.repository.OrganizationsRepository;
import pwr.zpi.organization.datasource.ShardManagementUtils;
import pwr.zpi.organization.entity.pub.Organization;

import java.util.UUID;

@Service
@Log4j2
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationsService{

    final OrganizationsRepository organizationsRepository;
    final IssueRepository issueRepository;

    @Override
    public Organization createOrganization(OrganizationDto organization) {
        var newOrg = new Organization();
        newOrg.setAddress(organization.address());
        newOrg.setName(organization.name());
        newOrg.setId(UUID.randomUUID());
        newOrg = organizationsRepository.save(newOrg);
        ShardManagementUtils.createNewOrganization(newOrg.getId());
        return newOrg;
    }

    @Override
    public Issue addIssue(String name) {
        return issueRepository.save(new Issue(Integer.toUnsignedLong(UUID.randomUUID().hashCode()), name));
    }

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
