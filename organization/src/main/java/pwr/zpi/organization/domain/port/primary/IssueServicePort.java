package pwr.zpi.organization.domain.port.primary;

import pwr.zpi.organization.application.adpter.primary.IssueServiceAdapter;
import pwr.zpi.organization.domain.model.dto.IssueDto;
import pwr.zpi.organization.domain.model.org.Issue;
import pwr.zpi.organization.domain.port.mapper.IssueMapper;
import pwr.zpi.organization.domain.port.secondary.IssueRepositoryPort;

import java.util.UUID;

public class IssueServicePort implements IssueServiceAdapter {

    private final IssueRepositoryPort issueRepository;
    private final IssueMapper issueMapper = IssueMapper.INSTANCE;

    public IssueServicePort(IssueRepositoryPort issueRepository) {
        this.issueRepository = issueRepository;
    }

    @Override
    public IssueDto addIssue(String name) {
        return issueMapper.issueToIssueDto(addIssueHelper(name));
    }

    public Issue addIssueHelper(String name) {
        Issue issue = Issue.builder()
                .name(name)
                .id(Integer.toUnsignedLong(UUID.randomUUID().hashCode()))
                .build();
        IssueDto issueDtoBefore = issueMapper.issueToIssueDto(issue);
        IssueDto issueDto = issueRepository.save(issueDtoBefore);
        return issueMapper.issueDtoToIssue(issueDto);
    }

}
