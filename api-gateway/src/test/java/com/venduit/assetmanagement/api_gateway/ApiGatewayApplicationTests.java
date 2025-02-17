package com.venduit.assetmanagement.api_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venduit.assetmanagement.api_gateway.POJO.AuthenticationRequest;
import com.venduit.assetmanagement.api_gateway.POJO.AuthenticationResponse;
import com.venduit.assetmanagement.api_gateway.POJO.User;
import com.venduit.assetmanagement.api_gateway.client.UserClient;
import com.venduit.assetmanagement.api_gateway.service.AuthenticationService;
import com.venduit.assetmanagement.api_gateway.service.JwtService;
import com.venduit.assetmanagement.api_gateway.util.LoggingService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiGatewayApplicationTests {

	@LocalServerPort
	private Integer port;

	private ObjectMapper objectMapper;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private UserClient userClient;

	@Mock
	private JwtService jwtService;

	@Mock
	private LoggingService loggingService;

	@InjectMocks
	private AuthenticationService authenticationService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	@Test
	void testAuthenticate_Success() {
		// Arrange
		AuthenticationRequest request = new AuthenticationRequest("test@example.com", "@Password123");
		User user = new User();
		user.setEmail("test@example.com");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(null); // AuthenticationManager returns null on success
		when(userClient.getUserDetailsByEmail("test@example.com")).thenReturn(user);
		when(jwtService.generateToken(user)).thenReturn("jwtToken");
		when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

		// Act
		AuthenticationResponse response = authenticationService.authenticate(request);

		// Assert
		assertNotNull(response);
		assertEquals("jwtToken", response.getAccessToken());
		assertEquals("refreshToken", response.getRefreshToken());

		// Verify interactions
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(userClient).getUserDetailsByEmail("test@example.com");
		verify(jwtService).generateToken(user);
		verify(jwtService).generateRefreshToken(user);
		verify(loggingService).logAction("User authenticated", "test@example.com", "User successfully authenticated");
		verify(authenticationService).revokeAllUserTokens(user);
		verify(authenticationService).saveUserToken(user, "jwtToken");
	}

	@Test
	void testAuthenticate_Failure() {
		// Arrange
		AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrongpassword");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new AuthenticationException("Invalid credentials") {});

		// Act & Assert
		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			authenticationService.authenticate(request);
		});

		assertEquals("Could not authenticate user. Please try again.", exception.getMessage());

		// Verify interactions
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		verify(loggingService).logError("Auth error", "test@example.com", "Could not authenticate user. Please try again.");
		verifyNoInteractions(userClient, jwtService, authenticationService); // Ensure no other interactions occurred
	}
}
