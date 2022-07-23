package pwr.zpi.organization.domain.model.org;

import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@Builder
public class Issue {

    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Issue issue = (Issue) o;
        return id != null && Objects.equals(id, issue.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
