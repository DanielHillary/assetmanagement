package com.venduit.assetmanagement.investment_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Investment {
    @Id
    @GeneratedValue
    private Integer id;
    private String investmentName;
    private String description;
    private double tenure;
    private double expectedRate;
    private double minimumAmount;
}
