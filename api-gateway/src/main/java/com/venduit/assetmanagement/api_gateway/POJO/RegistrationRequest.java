package com.venduit.assetmanagement.api_gateway.POJO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    private String firstname;
    private String lastname;
    private String userName;
    private String email;
    private String password;
    private String phoneNumber;
    private String role;
}