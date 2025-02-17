package com.venduit.assetmanagement.investment_service.repository;

import com.venduit.assetmanagement.investment_service.model.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Integer> {
}
