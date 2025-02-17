package com.venduit.assetmanagement.transaction_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Disbursement extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amountDisbursed;
    private LocalDateTime timeOfDisbursement;
    private Long borrowerId;
    private Long issuerId;
    private int accountNumber;
    private Long loanId;
}
