package pwr.zpi.organization.domain.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto implements Serializable {
    private UUID id;
    private String name;
    private String address;
}
