package com.venduit.assetmanagement.api_gateway.client;

import com.venduit.assetmanagement.api_gateway.POJO.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;
import java.util.Optional;

public interface UserClient {

    @GetExchange("/getuserbyusername")
    User getUserDetailsByUserName(@RequestParam String username);

    @GetExchange("/getuserbyemail")
    User getUserDetailsByEmail(@RequestParam String email);

    @PostExchange(value = "/registeruser")
    ResponseEntity<StandardResponse> registerUser(@RequestBody RegistrationRequest request);

    @PostExchange(value = "/auth/savetoken")
    void saveUserToken(@RequestBody Token token);

    @GetExchange(value = "/auth/getvalidtokenforuser")
    List<Token> getValidTokensForUser(@RequestParam int id);

    @GetExchange(value = "/auth/gettokenbyjwt")
    Optional<Token> getTokenByJwt(@RequestParam String jwt);

}
