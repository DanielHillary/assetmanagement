package com.venduit.assetmanagement.transaction_service.controller;

import com.venduit.assetmanagement.transaction_service.model.POJO.StandardResponse;
import com.venduit.assetmanagement.transaction_service.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/getusertransactions")
    public ResponseEntity<StandardResponse> getUserTransactions(@RequestParam("id") Long id){
        return transactionService.getUserTransactions(id, Pageable.ofSize(15));
    }
}
