package com.pwr.zpi.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupSample {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    private String name;
}
