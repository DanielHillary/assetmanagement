package com.venduit.assetmanagement.api_gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venduit.assetmanagement.api_gateway.POJO.*;
import com.venduit.assetmanagement.api_gateway.client.UserClient;
import com.venduit.assetmanagement.api_gateway.POJO.RegistrationRequest;
import com.venduit.assetmanagement.api_gateway.POJO.StandardResponse;
import com.venduit.assetmanagement.api_gateway.util.LoggingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserClient userClient;
    private final LoggingService loggingService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            var user = userClient.getUserDetailsByEmail(request.getEmail());
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);
            System.out.println("User: " + user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            loggingService.logAction("User authenticated", request.getEmail(), "User successfully authenticated");
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (RuntimeException e) {
            loggingService.logError("Auth error", request.getEmail(), "Could not authenticate user. Please try again.");
            throw new RuntimeException(e);
        }
    }

    public void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        userClient.saveUserToken(token);
    }

    public void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        var validUserTokens = userClient.getValidTokensForUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            userClient.saveUserToken(token); // Work on sending the entire tokens at once to save making network calls
        });
//        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userClient.getUserDetailsByEmail(userEmail);
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public ResponseEntity<StandardResponse> register(RegistrationRequest request) {
        try {
            return StandardResponse.sendHttpResponse(true, "Successful", Objects.requireNonNull(userClient.registerUser(request).getBody()).getData(), HttpStatus.CREATED);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encodeDetails(String details) {
        BCryptPasswordEncoder otppasswordEncoder = new BCryptPasswordEncoder();
        return otppasswordEncoder.encode(details);
    }
}
