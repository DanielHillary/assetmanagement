package com.venduit.assetmanagement.transaction_service.model.POJO;

public record DisbursementRequest (Double amount, Long loanId, Long accountId, Long creditAccount,
                                    String description, Long issuerId){
}
