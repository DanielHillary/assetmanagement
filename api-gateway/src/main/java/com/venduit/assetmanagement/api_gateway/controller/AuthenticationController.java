package com.venduit.assetmanagement.api_gateway.controller;


import com.venduit.assetmanagement.api_gateway.POJO.*;
import com.venduit.assetmanagement.api_gateway.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<StandardResponse> register(
            @RequestBody RegistrationRequest request
    ) {
        return service.register(request);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @GetMapping("/encodestring")
    public String encodeDetails(@RequestParam("details") String details){
        return service.encodeDetails(details);
    }

    @GetMapping("/check")
    public String checkingStatus(){
        return "Service is Working";
    }

}
