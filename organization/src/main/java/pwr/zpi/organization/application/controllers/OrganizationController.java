package pwr.zpi.organization.application.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pwr.zpi.organization.application.adpter.primary.IssueServiceAdapter;
import pwr.zpi.organization.application.adpter.primary.OrganizationServiceAdapter;
import pwr.zpi.organization.application.view_model.OrganizationCreationViewModel;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;

import java.util.UUID;


@RestController
@RequestMapping(path = "/organization",  produces = "application/json; charset=UTF-8")
public class OrganizationController {

    final
    OrganizationServiceAdapter organizationService;
    final
    IssueServiceAdapter issueServiceAdapter;

    public OrganizationController(OrganizationServiceAdapter organizationService, IssueServiceAdapter issueServiceAdapter) {
        this.organizationService = organizationService;
        this.issueServiceAdapter = issueServiceAdapter;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrganizationDto create(@RequestBody OrganizationCreationViewModel resource) {
        OrganizationDto organizationDto = new OrganizationDto(null, resource.getName(), resource.getAddress());
        return organizationService.createOrganization(organizationDto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public void delete(@RequestParam UUID orgId) {
        organizationService.deleteOrganization(orgId);
    }
//
//    @PostMapping("/addIssue")
//    @ResponseStatus(HttpStatus.CREATED)
//    public IssueDto addIssue(@RequestBody String name) {
//        return issueService.addIssue(name);
//    }
}
