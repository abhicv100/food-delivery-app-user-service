package com.bits.pilani.user_service.controller;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bits.pilani.user_service.security.JwtAuthHandlerInterceptor;
import com.bits.pilani.user_service.service.AuthService;
import com.bits.pilani.user_service.to.UsernamePasswordTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = {AuthController.class})
public class AuthControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	AuthService authService;
	
	@MockBean
	JwtAuthHandlerInterceptor jwtInterceptor;
	
	@Autowired
	ObjectMapper mapper;
	
	@BeforeEach
	void before() throws Exception {
		when(jwtInterceptor.preHandle(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(true);
	}
	
	@Order(1)
	@Test
	void testAuthenticateUser() throws Exception {		
		UsernamePasswordTO usernamePasswordTO = new UsernamePasswordTO();
		usernamePasswordTO.setUsername("user");
		usernamePasswordTO.setPassword("1234");
		
		when(authService.authenticateAndGetToken(Mockito.any())).thenReturn("token");
		
		String requestBody = mapper.writeValueAsString(usernamePasswordTO);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/auth")
							.contentType(MediaType.APPLICATION_JSON)
							.content(requestBody)
							).andExpect(MockMvcResultMatchers.status().isOk());
	}
}
