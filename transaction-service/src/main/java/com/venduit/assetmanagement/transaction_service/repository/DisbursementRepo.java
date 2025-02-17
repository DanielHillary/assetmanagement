package com.venduit.assetmanagement.transaction_service.repository;


import com.venduit.assetmanagement.transaction_service.model.Disbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DisbursementRepo extends JpaRepository<Disbursement, Long> {
}
