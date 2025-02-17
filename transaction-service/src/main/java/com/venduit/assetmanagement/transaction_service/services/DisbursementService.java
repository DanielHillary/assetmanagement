package com.venduit.assetmanagement.transaction_service.services;

import com.venduit.assetmanagement.transaction_service.model.Disbursement;
import com.venduit.assetmanagement.transaction_service.model.POJO.*;
import com.venduit.assetmanagement.transaction_service.model.Transaction;
import com.venduit.assetmanagement.transaction_service.repository.DisbursementRepo;
import com.venduit.assetmanagement.transaction_service.util.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DisbursementService {
    private final DisbursementRepo disbursementRepo;
    private final LoggingService loggingService;
    private final TransactionService transactionService;

    public ResponseEntity<StandardResponse> disburseMoney(DisbursementRequest request) {
        try {
            Disbursement disbursement = new Disbursement();
            TransactionRequest transactionRequest = new TransactionRequest(request.issuerId(), request.amount(),
                    request.loanId(), request.description(), request.creditAccount());
            Transaction transaction = transactionService.processDisbursementTransaction(transactionRequest);
            disbursement.setAmountDisbursed(transaction.getAmount());
            disbursement.setTimeOfDisbursement(LocalDateTime.now());
            disbursement.setLoanId(request.loanId());
            disbursement.setBorrowerId(request.creditAccount());
            disbursement.setIssuerId(request.issuerId());

            disbursementRepo.save(disbursement);

            loggingService.logAction("Saving disbursement", String.valueOf(request.issuerId()), "Disbursed funds to user");
            return StandardResponse.sendHttpResponse(true, "Successful", disbursement, HttpStatus.OK);
        } catch (Exception e) {
            loggingService.logError("Error","An error occured", e.getMessage());
            return StandardResponse.sendFailedHttpResponse(false, "Something went wrong");
        }
    }
}
