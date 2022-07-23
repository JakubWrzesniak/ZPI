package pwr.zpi.organization.domain.port.secondary;


import pwr.zpi.organization.domain.model.dto.IssueDto;

public interface IssueRepositoryPort {
    IssueDto save(IssueDto issue);
}
