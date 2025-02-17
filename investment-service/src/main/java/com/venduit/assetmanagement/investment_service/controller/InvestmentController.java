package com.venduit.assetmanagement.investment_service.controller;

import com.venduit.assetmanagement.investment_service.model.POJO.StandardResponse;
import com.venduit.assetmanagement.investment_service.services.InvestmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/investments")
@RequiredArgsConstructor
@RestController
public class InvestmentController {

    private final InvestmentService investmentService;

    @GetMapping("/getinvestmentplans")
    public ResponseEntity<StandardResponse> getAllInvestmentPlan(){
        return investmentService.getAllInvestmentPlan();
    }
}
