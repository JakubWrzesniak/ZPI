package pwr.zpi.organization.application.adpter.primary;

import org.springframework.stereotype.Component;
import pwr.zpi.organization.domain.model.dto.IssueDto;

@Component
public interface IssueServiceAdapter {
    IssueDto addIssue(String name);
}
