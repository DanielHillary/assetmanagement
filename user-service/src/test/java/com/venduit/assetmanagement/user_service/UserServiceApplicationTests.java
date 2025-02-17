package com.venduit.assetmanagement.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.venduit.assetmanagement.user_service.client.GatewayClient;
import com.venduit.assetmanagement.user_service.controller.UserController;
import com.venduit.assetmanagement.user_service.model.POJO.RegistrationRequest;
import com.venduit.assetmanagement.user_service.model.POJO.StandardResponse;
import com.venduit.assetmanagement.user_service.model.Role;
import com.venduit.assetmanagement.user_service.model.User;
import com.venduit.assetmanagement.user_service.repository.UserRepository;
import com.venduit.assetmanagement.user_service.service.UserService;
import com.venduit.assetmanagement.user_service.service.ValidationService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.MySQLContainer;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class UserServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:8.3.0");
	@LocalServerPort
	private Integer port;

	private ObjectMapper objectMapper;

	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		objectMapper = new ObjectMapper();
	}

	static {
		mySQLContainer.start();
	}
	@Mock
	private UserRepository userRepo;

	@Mock
	private ValidationService validationService;

	@Mock
	private GatewayClient gatewayClient;

	@InjectMocks
	private UserService registrationService;

	@Test
	void testRegisterSuccess() throws Exception {
		RegistrationRequest request = createRegistrationRequest();
		given()
				.contentType(ContentType.JSON)
				.body(objectMapper.writeValueAsString(request)) // Serialize request to JSON
				.when()
				.post("/api/v1/user/registeruser") // Your registration endpoint
				.then()
				.statusCode(HttpStatus.CREATED.value())
				.body("status", equalTo(true))
				.body("message", equalTo("Successfully created user"))
				.body("data", notNullValue());
	}

	@Test
	void testRegisterUsernameExists() {
		RegistrationRequest request = createRegistrationRequest();
		User existingUser = createUser();
		existingUser.setUsername(request.getUserName());
		when(userRepo.findByUsername(request.getUserName())).thenReturn(Optional.of(existingUser));

		ResponseEntity<StandardResponse> response = registrationService.register(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(Objects.requireNonNull(response.getBody()).isStatus());
		assertEquals("Invalid email", response.getBody().getMessage());
	}

	@Test
	void testRegisterEmailExists() {
		RegistrationRequest request = createRegistrationRequest();
		User existingUser = createUser();
		existingUser.setEmail(request.getEmail());
		when(userRepo.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

		ResponseEntity<StandardResponse> response = registrationService.register(request);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertFalse(Objects.requireNonNull(response.getBody()).isStatus());
		assertEquals("Invalid email", response.getBody().getMessage());
	}

//	@Test
//	void testRegisterEmailExists() throws Exception {
//		RegistrationRequest request = createRegistrationRequest();
//		// Assuming you have a way to pre-populate the database for this test
//
//		given()
//				.contentType(ContentType.JSON)
//				.body(objectMapper.writeValueAsString(request))
//				.when()
//				.post("/api/v1/user/registeruser")
//				.then()
//				.statusCode(HttpStatus.BAD_REQUEST.value()) // Or appropriate error code
//				.body("status", equalTo(false))
//				.body("message", equalTo("This email is already registered with us."));
//	}

	@Test
	public void testRegister_InvalidEmail() {
		// Arrange
		RegistrationRequest request = new RegistrationRequest();
		request.setUserName("testuser");
		request.setEmail("invalid-email");
		request.setPassword("Password123!");
		request.setPhoneNumber("1234567890");
		request.setFirstname("John");
		request.setLastname("Doe");
		request.setRole("USER");

		when(userRepo.findByUsername("testuser")).thenReturn(Optional.empty());
		when(validationService.validateEmail("invalid-email")).thenReturn(false);

		// Act
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.post("/api/v1/user/registeruser");

		// Assert
		response.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("status", equalTo(false))
				.body("message", equalTo("Invalid email"));
	}

	@Test
	public void testRegister_InvalidPassword() {
		// Arrange
		RegistrationRequest request = new RegistrationRequest();
		request.setUserName("testuser");
		request.setEmail("test@example.com");
		request.setPassword("weak");
		request.setPhoneNumber("1234567890");
		request.setFirstname("John");
		request.setLastname("Doe");
		request.setRole("USER");

		when(userRepo.findByUsername("testuser")).thenReturn(Optional.empty());
		when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());
		when(validationService.validateEmail("test@example.com")).thenReturn(true);
		when(validationService.validatePassword("weak")).thenReturn(false);

		// Act
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.post("/api/v1/user/registeruser");

		// Assert
		response.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("status", equalTo(false))
				.body("message", equalTo("Please follow the guideline for the password"));
	}

	@Test
	public void testRegister_InvalidPhoneNumber() {
		// Arrange
		RegistrationRequest request = new RegistrationRequest();
		request.setUserName("testuser");
		request.setEmail("test@example.com");
		request.setPassword("Password123!");
		request.setPhoneNumber("invalid");
		request.setFirstname("John");
		request.setLastname("Doe");
		request.setRole("USER");

		when(userRepo.findByUsername("testuser")).thenReturn(Optional.empty());
		when(userRepo.findByEmail("test@example.com")).thenReturn(Optional.empty());
		when(validationService.validateEmail("test@example.com")).thenReturn(true);
		when(validationService.validatePassword("Password123!")).thenReturn(true);
		when(validationService.validatePhoneNumber("invalid")).thenReturn(false);

		// Act
		Response response = given()
				.contentType(ContentType.JSON)
				.body(request)
				.post("/api/v1/user/registeruser");

		// Assert
		response.then()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("status", equalTo(false))
				.body("message", equalTo("Invalid phone number"));
	}

	private RegistrationRequest createRegistrationRequest() {
		RegistrationRequest request = new RegistrationRequest();
		request.setFirstname("Testing");
		request.setLastname("User1");
		request.setEmail("testing@example.com");
		request.setPhoneNumber("1234567890");
		request.setUserName("testinguser");
		request.setPassword("Password123!"); // Example password
		request.setRole("USER");
		return request;
	}

	private User createUser() {
		User user = new User();
		user.setFirstName("Testing");
		user.setLastName("User1");
		user.setEmail("testing@example.com");
		user.setPhoneNumber("1234567890");
		user.setUsername("testinguser");
		user.setPassword("encodedPassword");
		user.setRole(Role.USER);
		return user;
	}

}
