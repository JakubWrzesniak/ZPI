package pwr.zpi.organization.domain.model.pub;

import lombok.*;

import java.util.UUID;

@Data
@Builder
public class Organization {

    private UUID id;
    private String name;
    private String address;
}
