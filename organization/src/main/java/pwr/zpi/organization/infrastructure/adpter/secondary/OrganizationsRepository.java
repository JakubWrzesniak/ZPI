package pwr.zpi.organization.infrastructure.adpter.secondary;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pwr.zpi.organization.infrastructure.entity.pub.Organization;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrganizationsRepository extends JpaRepository<Organization, Integer> {
    Organization save(Organization organization);

    Optional<Organization> findByIdEquals(UUID id);


}
