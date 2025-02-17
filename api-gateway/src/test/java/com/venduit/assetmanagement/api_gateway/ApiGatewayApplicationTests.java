package com.venduit.assetmanagement.api_gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.venduit.assetmanagement.api_gateway.POJO.*;
import com.venduit.assetmanagement.api_gateway.client.UserClient;
import com.venduit.assetmanagement.api_gateway.service.AuthenticationService;
import com.venduit.assetmanagement.api_gateway.service.JwtService;
import com.venduit.assetmanagement.api_gateway.util.LoggingService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

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
		objectMapper = new ObjectMapper();
		RestAssured.registerParser("text/plain", Parser.JSON);
	}

	@Test
	void testAuthenticateSuccess() throws JsonProcessingException {
		// Given
		AuthenticationRequest request = new AuthenticationRequest("test@example.com", "Password123!");

		String encodedPassword = new BCryptPasswordEncoder().encode("Password123!"); // Simulating encoded password
		User mockUser = new User(1, "test@example.com", encodedPassword, Role.USER);

		when(userClient.getUserDetailsByEmail(request.getEmail())).thenReturn(mockUser);

		var mockUserDetails = new User(
				"test@example.com",
				encodedPassword,
				List.of(new SimpleGrantedAuthority("ROLE_USER"))
		);

		var authToken = new UsernamePasswordAuthenticationToken(
				mockUserDetails,
				request.getPassword(),
				mockUserDetails.getAuthorities()
		);

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(authToken);

		given()
				.contentType(ContentType.JSON)
				.body(objectMapper.writeValueAsString(request)) // Serialize request to JSON
				.when()
				.post("api/v1/auth/authenticate")
				.then()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	void testAuthenticate_Failure() {
		// Arrange
		AuthenticationRequest request = new AuthenticationRequest("test@example.com", "wrongpassword");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenThrow(new AuthenticationException("Invalid credentials") {});

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			authenticationService.authenticate(request);
		});

		assertFalse(exception.getMessage().contains("Could not authenticate user. Please try again."));

		verify(authenticationManager, never()).authenticate(any(UsernamePasswordAuthenticationToken.class));

		verify(loggingService, never()).logAction(anyString(), anyString(), anyString());

		// Also, verify that no interactions occur with userClient and jwtService.
		verifyNoInteractions(userClient, jwtService, authenticationManager);
	}

}
