package com.venduit.assetmanagement.transaction_service.model.POJO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserContext {
    private String userId;
    private List<String> roles;
}