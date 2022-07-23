package pwr.zpi.organization.domain.model.dto;

import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDto implements Serializable {
    private String id;
    private String name;
    private String address;
}
