package com.venduit.assetmanagement.investment_service.services;

import com.venduit.assetmanagement.investment_service.model.POJO.StandardResponse;
import com.venduit.assetmanagement.investment_service.repository.InvestmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestmentService {

    private final InvestmentRepository investmentRepository;

    public ResponseEntity<StandardResponse> getAllInvestmentPlan() {
        try {
            return StandardResponse.sendHttpResponse(true, "Successful", investmentRepository.findAll(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return StandardResponse.sendFailedHttpResponse(false, "Something went wrong, please try again.");
        }
    }
}
