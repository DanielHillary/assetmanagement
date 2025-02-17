package com.venduit.assetmanagement.transaction_service.model.POJO;

public record TransactionRequest(Long userId, double amount, Long loanId,
                                 String description, Long creditAccount) {
}
