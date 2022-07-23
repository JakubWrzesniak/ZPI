package pwr.zpi.organization.infrastructure.adpter.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import pwr.zpi.organization.domain.model.dto.IssueDto;
import pwr.zpi.organization.infrastructure.entity.org.Issue;


@Mapper
public interface IssueMapper {

    IssueMapper INSTANCE = Mappers.getMapper( IssueMapper.class );

    IssueDto issueToIssueDto(Issue issue);
    Issue issueDtoToIssue(IssueDto issueDto);
}
