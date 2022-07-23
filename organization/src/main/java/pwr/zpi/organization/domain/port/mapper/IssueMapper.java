package pwr.zpi.organization.domain.port.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pwr.zpi.organization.domain.model.dto.IssueDto;
import pwr.zpi.organization.domain.model.org.Issue;

@Mapper
public interface IssueMapper {

    IssueMapper INSTANCE = Mappers.getMapper( IssueMapper.class );

    IssueDto issueToIssueDto(Issue issue);
    Issue issueDtoToIssue(IssueDto issueDto);
}
