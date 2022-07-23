package pwr.zpi.organization.domain.model.org;

import lombok.*;

import java.util.Objects;

@Data
@Builder
public class Client {
    private Long id;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Client client)) return false;
        return id.equals(client.id) && name.equals(client.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
