package com.silu.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class RestaurantDto {
    private Long id;
    private String title;

    @Column(length = 1000)
    private List<String> images;

    private String description;


}
