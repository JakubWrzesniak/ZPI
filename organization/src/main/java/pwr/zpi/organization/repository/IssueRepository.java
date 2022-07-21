package pwr.zpi.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.zpi.organization.entity.org.Issue;

public interface IssueRepository extends JpaRepository<Issue, Long> {

}
