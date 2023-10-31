// package com.app.gamereview.controllers;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
// import static org.mockito.Mockito.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// import java.util.Calendar;
// import java.util.Date;

// import com.app.gamereview.controller.AuthController;
// import com.app.gamereview.dto.request.VerifyResetCodeRequestDto;
// import com.app.gamereview.model.ResetCode;
// import com.app.gamereview.model.User;
// import com.app.gamereview.repository.ResetCodeRepository;
// import com.app.gamereview.repository.UserRepository;
// import com.app.gamereview.service.AuthService;
// import com.app.gamereview.service.EmailService;
// import com.app.gamereview.service.UserService;
// import com.fasterxml.jackson.databind.ObjectMapper;

// @WebMvcTest(AuthController.class)
// public class AuthControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @MockBean
//     private UserService userService;

//     @MockBean
//     private AuthService authService;

//     @MockBean
//     private EmailService emailService;

//     @MockBean
//     private UserRepository userRepository; // Add the UserRepository mock

//     @MockBean
//     ResetCodeRepository resetCodeRepository;

//     @Test
//     public void testVerifyResetCodeEndpoint() throws Exception {
//         // Arrange
//         String code = "validCode";
//         String email = "valid@example.com";

//         User user = new User();
//         user.setEmail(email);

//         String id = user.getId();

//         Calendar calendar = Calendar.getInstance(); // Gets a calendar using the default time zone and locale.
//         calendar.add(Calendar.DAY_OF_YEAR, 1); // Moves the calendar one day forward (tomorrow).
//         Date tomorrow = calendar.getTime();

//         ResetCode resetCode = new ResetCode(code, email, tomorrow);

//         VerifyResetCodeRequestDto request = new VerifyResetCodeRequestDto(code, email);

//         // Stub the service method to return the desired result
//         when(authService.getResetCodeByCode(code)).thenReturn(resetCode);
//         when(userService.getUserById(id)).thenReturn(user);

//         // Convert DTO object to JSON string using ObjectMapper
//         ObjectMapper objectMapper = new ObjectMapper();
//         String jsonRequestBody = objectMapper.writeValueAsString(request);

//         // Act & Assert
//         mockMvc.perform(post("/api/auth/verify-reset-code")
//                 .contentType("application/json")
//                 .content(jsonRequestBody))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Reset code verified successfully"));

//         // Verify that the service method was called with the correct parameters
//         verify(authService, times(1)).getResetCodeByCode(code);
//         verify(userService, times(1)).getUserById(id);
//     }

//     @Test
//     public void testVerifyResetCodeNotFound() throws Exception {
//         // Arrange
//         String code = "asfasdf";
//         String email = "redundant@example.com";
//         String id = "asşdknas";

//         // Stub the service method to return the desired result
//         when(authService.getResetCodeByCode(code)).thenReturn(null);

//         VerifyResetCodeRequestDto request = new VerifyResetCodeRequestDto(code, email);

//         // Convert DTO object to JSON string using ObjectMapper
//         ObjectMapper objectMapper = new ObjectMapper();
//         String jsonRequestBody = objectMapper.writeValueAsString(request);

//         // Act & Assert
//         mockMvc.perform(post("/api/auth/verify-reset-code")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(jsonRequestBody))
//                 .andExpect(status().isNotFound())
//                 .andExpect(content().string("Invalid or expired reset code"));

//         // Verify that the service method was called with the correct parameters
//         verify(authService, times(1)).getResetCodeByCode(code);
//         verify(userService, times(0)).getUserById(id);
//     }

//     @Test
//     public void testVerifyResetCodeExpired() throws Exception {
//         // Arrange
//         String code = "validCode";
//         String email = "valid@example.com";

//         User user = new User();
//         user.setEmail(email);

//         String id = user.getId();

//         Calendar calendar = Calendar.getInstance(); // Gets a calendar using the default time zone and locale.
//         calendar.add(Calendar.DAY_OF_YEAR, -1); // Moves the calendar one day forward (tomorrow).
//         Date tomorrow = calendar.getTime();

//         ResetCode resetCode = new ResetCode(code, email, tomorrow);

//         VerifyResetCodeRequestDto request = new VerifyResetCodeRequestDto(code, email);

//         // Stub the service method to return the desired result
//         when(authService.getResetCodeByCode(code)).thenReturn(resetCode);
//         when(userService.getUserById(id)).thenReturn(user);

//         // Convert DTO object to JSON string using ObjectMapper
//         ObjectMapper objectMapper = new ObjectMapper();
//         String jsonRequestBody = objectMapper.writeValueAsString(request);

//         // Act & Assert
//         mockMvc.perform(post("/api/auth/verify-reset-code")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(jsonRequestBody))
//                 .andExpect(status().isOk())
//                 .andExpect(content().string("Invalid or expired reset code"));

//         // Verify that the service method was called with the correct parameters
//         verify(authService, times(1)).getResetCodeByCode(code);
//         verify(userService, times(0)).getUserById(id);
//     }

//     @Test
//     public void testVerifyResetCodeEmailMismatch() throws Exception {
//         // Arrange
//         String code = "validCode";
//         String codeEmail = "mismatched@example.com";
//         String userEmail = "user@example.com";

//         User user = new User();
//         user.setEmail(userEmail);

//         String id = user.getId();

//         Calendar calendar = Calendar.getInstance(); // Gets a calendar using the default time zone and locale.
//         calendar.add(Calendar.DAY_OF_YEAR, 1); // Moves the calendar one day forward (tomorrow).
//         Date tomorrow = calendar.getTime();

//         ResetCode resetCode = new ResetCode(code, codeEmail, tomorrow);

//         VerifyResetCodeRequestDto request = new VerifyResetCodeRequestDto(code, codeEmail);

//         // Stub the service method to return the desired result
//         when(authService.getResetCodeByCode(code)).thenReturn(resetCode);
//         when(userService.getUserById(id)).thenReturn(user);

//         // Convert DTO object to JSON string using ObjectMapper
//         ObjectMapper objectMapper = new ObjectMapper();
//         String jsonRequestBody = objectMapper.writeValueAsString(request);

//         // Act & Assert
//         mockMvc.perform(post("/api/auth/verify-reset-code")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("{\"resetCode\":\"" + code + "\",\"userEmail\":\"" + userEmail + "\"}"))
//                 .andExpect(status().isBadRequest())
//                 .andExpect(content().string("Email and Code doesn't match"));

//         // Verify that the service method was called with the correct parameters
//         verify(authService, times(1)).getResetCodeByCode(code);
//         verify(userService, times(1)).getUserById(id);
//     }

// }
