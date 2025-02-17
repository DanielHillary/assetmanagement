package com.venduit.assetmanagement.user_service.controller;


import com.venduit.assetmanagement.user_service.model.Token;
import com.venduit.assetmanagement.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @GetMapping("/getvalidtokenforuser")
    public List<Token> getValidTokensForUser(@RequestParam("id") int id){
        return authService.getAllValidTokenForUser(id);
    }

    @GetMapping(value = "/gettokenbyjwt")
    public Optional<Token> getTokenByJwt(@RequestParam String jwt){
        return authService.getTokenByJwt(jwt);
    }


    @PostMapping(value = "/savetoken")
    public void saveUserToken(@RequestBody Token token){
        authService.saveToken(token);
    }
}
