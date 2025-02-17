package com.venduit.assetmanagement.transaction_service.controller;

import com.venduit.assetmanagement.transaction_service.model.POJO.DisbursementRequest;
import com.venduit.assetmanagement.transaction_service.model.POJO.StandardResponse;
import com.venduit.assetmanagement.transaction_service.services.DisbursementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/disbursement")
public class DisbursementController {

    private final DisbursementService disbursementService;

    @PostMapping("/disbursetouser")
    public ResponseEntity<StandardResponse> disburseMoney(@RequestBody DisbursementRequest request){
        return disbursementService.disburseMoney(request);
    }
}
