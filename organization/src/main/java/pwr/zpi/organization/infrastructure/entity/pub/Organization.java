package pwr.zpi.organization.infrastructure.entity.pub;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Organization {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;
    private String name;
    private String address;
}
