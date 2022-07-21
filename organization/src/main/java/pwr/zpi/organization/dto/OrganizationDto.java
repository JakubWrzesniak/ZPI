package pwr.zpi.organization.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.io.Serializable;

public record OrganizationDto(String name, String address) implements Serializable {
}
