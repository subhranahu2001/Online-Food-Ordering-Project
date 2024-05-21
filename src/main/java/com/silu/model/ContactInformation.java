package com.silu.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class ContactInformation {

    private String email;

    private String mobile;

    private String twitter;

    private String facebook;

    private String instagram;
}
