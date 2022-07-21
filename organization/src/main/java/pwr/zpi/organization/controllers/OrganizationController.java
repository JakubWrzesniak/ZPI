package pwr.zpi.organization.controllers;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pwr.zpi.organization.dto.OrganizationDto;
import pwr.zpi.organization.entity.org.Issue;
import pwr.zpi.organization.entity.pub.Organization;
import pwr.zpi.organization.services.OrganizationsService;

@RestController
@RequestMapping(path = "/organization",  produces = "application/json; charset=UTF-8")
public class OrganizationController {

    @Autowired
    OrganizationsService organizationsService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public Organization create(@RequestBody OrganizationDto resource) {
        return organizationsService.createOrganization(resource);
    }

    @PostMapping("/addIssue")
    @ResponseStatus(HttpStatus.CREATED)
    public Issue addIssue(@RequestBody String name) {
        return organizationsService.addIssue(name);
    }
}
