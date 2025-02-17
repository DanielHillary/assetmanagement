package com.venduit.assetmanagement.transaction_service.services;

import com.venduit.assetmanagement.transaction_service.event.TransactionEvent;
import com.venduit.assetmanagement.transaction_service.model.Account;
import com.venduit.assetmanagement.transaction_service.model.POJO.StandardResponse;
import com.venduit.assetmanagement.transaction_service.model.POJO.TransactionRequest;
import com.venduit.assetmanagement.transaction_service.model.Transaction;
import com.venduit.assetmanagement.transaction_service.repository.AccountRepository;
import com.venduit.assetmanagement.transaction_service.repository.TransactionRepository;
import com.venduit.assetmanagement.transaction_service.util.LoggingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
//    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;
    private final LoggingService loggingService;

    private static final double tierOne = 10000.00;
    private static final double tierTwo = 25000.00;
    private static final double tierThree = 100000.00;


    public ResponseEntity<StandardResponse> getUserTransactions(Long userId, Pageable pageable) {
        try {
            return StandardResponse.sendHttpResponse(true, "Successful", transactionRepository.findByUserId(userId, pageable).getContent(), HttpStatus.OK);
        } catch (Exception e) {
            return StandardResponse.sendFailedHttpResponse(false, "Something went wrong");
        }

    }

    public Transaction processDisbursementTransaction(TransactionRequest request) {
        try {
            //Add Charges
            Double amount = request.amount();
            if (amount == 0) {
                throw new IllegalArgumentException("Transaction amount must be greater than zero.");
            } else if (amount < tierOne) {
                amount -= 10.00;
            } else if (amount < tierTwo && amount > tierOne) {
                amount -= 25.00;
            }

            Account account = accountRepository.findByUserId(request.userId())
                    .orElseThrow(() -> new IllegalArgumentException("Account not found for user ID: " + request.userId()));

            // Check if the transaction is a debit and validate balance
            if (amount < 0 && account.getBalance() + amount < 0) {
                throw new IllegalArgumentException("Insufficient funds for this transaction.");
            }

            //Transfer successful
            Transaction transaction = Transaction.builder()
                    .debitAccountId(request.userId())
                    .creditAccountId(request.creditAccount())
                    .amount(amount)
                    .description(request.description())
                    .transactionTime(LocalDateTime.now())
                    .build();

            // Update account balance
            account.setBalance(account.getBalance() + amount);
            accountRepository.save(account);

            loggingService.logAction("Transaction successful", String.valueOf(transaction.getId()), "Transfer to " + account.getAccountNumber() + " was successful");

            return transactionRepository.save(transaction);
        } catch (Exception e) {

            loggingService.logError("Error", "An error occured", "Transaction failed");
            return null;
        }
    }

}

