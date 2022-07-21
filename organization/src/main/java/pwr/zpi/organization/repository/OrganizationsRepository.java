package pwr.zpi.organization.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwr.zpi.organization.entity.pub.Organization;

@Repository
public interface OrganizationsRepository extends JpaRepository<Organization, Integer> {
}
