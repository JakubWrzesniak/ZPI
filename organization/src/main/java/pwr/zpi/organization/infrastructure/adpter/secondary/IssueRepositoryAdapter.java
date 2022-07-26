package pwr.zpi.organization.infrastructure.adpter.secondary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pwr.zpi.organization.domain.model.dto.IssueDto;
import pwr.zpi.organization.domain.port.secondary.IssueRepositoryPort;
import pwr.zpi.organization.infrastructure.adpter.mapper.IssueMapper;
import pwr.zpi.organization.infrastructure.entity.org.Issue;

@Component
public class IssueRepositoryAdapter implements IssueRepositoryPort {

    @Autowired
    private IssueRepository issueRepository;
    private final IssueMapper issueMapper = IssueMapper.INSTANCE;


    @Override
    public IssueDto save(IssueDto issueDto) {
        Issue issue = issueMapper.issueDtoToIssue(issueDto);
        Issue issueSaved = issueRepository.save(issue);
        return issueMapper.issueToIssueDto(issueSaved);
    }
}
