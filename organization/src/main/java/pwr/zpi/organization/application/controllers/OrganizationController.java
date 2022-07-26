package pwr.zpi.organization.application.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pwr.zpi.organization.application.adpter.primary.IssueServiceAdapter;
import pwr.zpi.organization.application.adpter.primary.OrganizationServiceAdapter;
import pwr.zpi.organization.domain.model.dto.IssueDto;
import pwr.zpi.organization.domain.model.dto.OrganizationDto;


@RestController
@RequestMapping(path = "/organization",  produces = "application/json; charset=UTF-8")
public class OrganizationController {

    final
    OrganizationServiceAdapter organizationsService;
    final
    IssueServiceAdapter issueServiceAdapter;

    public OrganizationController(OrganizationServiceAdapter organizationsService, IssueServiceAdapter issueServiceAdapter) {
        this.organizationsService = organizationsService;
        this.issueServiceAdapter = issueServiceAdapter;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrganizationDto create(@RequestBody OrganizationDto resource) {
        return organizationsService.createOrganization(resource);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String hello() {
        return "HII";
    }

    @PostMapping("/addIssue")
    @ResponseStatus(HttpStatus.CREATED)
    public IssueDto addIssue(@RequestBody String name) {
        return issueServiceAdapter.addIssue(name);
    }
}
