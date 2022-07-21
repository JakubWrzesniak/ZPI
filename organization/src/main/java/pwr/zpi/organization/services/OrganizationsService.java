package pwr.zpi.organization.services;

import pwr.zpi.organization.dto.OrganizationDto;
import pwr.zpi.organization.entity.org.Issue;
import pwr.zpi.organization.entity.pub.Organization;

public interface OrganizationsService {
    Organization createOrganization(OrganizationDto organization);


    Issue addIssue(String name);
}
