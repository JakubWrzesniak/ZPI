package pwr.zpi.organization.infrastructure.adpter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwr.zpi.organization.domain.port.secondary.IssueRepositoryPort;
import pwr.zpi.organization.infrastructure.entity.org.Issue;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, IssueRepositoryPort {

}
