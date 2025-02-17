package com.venduit.assetmanagement.saving_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Saving {
    @Id
    @GeneratedValue
    private Integer id;
    private String planName;
    private String description;
    private double tenure;
    private double estimatedInterest;
    private double minimumAmount;
    private double maximumAmount;
    private LocalDate startDate;
    private LocalDate endDate;
}
