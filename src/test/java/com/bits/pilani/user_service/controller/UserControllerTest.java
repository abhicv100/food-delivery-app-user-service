package com.bits.pilani.user_service.controller;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.bits.pilani.user_service.security.Role;
import com.bits.pilani.user_service.service.UserService;
import com.bits.pilani.user_service.to.UserTO;
import com.bits.pilani.user_service.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = { UserController.class })
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	UserService userService;


	@Autowired
	ObjectMapper mapper;

	MockedStatic<TokenUtil> mockedTokenUtil;
	
	@BeforeEach
	void before() throws Exception {		
		mockedTokenUtil = mockStatic(TokenUtil.class);
		mockedTokenUtil.when(() -> TokenUtil.validateUser(Mockito.anyString(), Mockito.anyInt())).thenReturn(true);		
	}
	
	@AfterEach
	void after() {
		mockedTokenUtil.close();
	}

	
	@Order(1)
	@Test
	void testGetUser() throws Exception {
		UserTO userTo = new UserTO();
		userTo.setId("673cb079ca3a4706c03db098");
		userTo.setUsername("user");
		userTo.setFullName("fullname");
		userTo.setAddress("address");
		userTo.setRole("CUSTOMER");

		when(userService.getUser(Mockito.anyString())).thenReturn(userTo);

		mockMvc.perform(MockMvcRequestBuilders.get("/user/1")).andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Order(2)
	@Test
	void testCreateUser() throws Exception {
		UserTO userTo = new UserTO();
		userTo.setId("673cb079ca3a4706c03db098");
		userTo.setUsername("user");
		userTo.setFullName("fullname");
		userTo.setAddress("address");
		userTo.setRole("CUSTOMER");

		var requestBody = mapper.writeValueAsString(userTo);

		when(userService.createUser(Mockito.any(UserTO.class))).thenReturn(userTo);

		mockMvc.perform(
				MockMvcRequestBuilders.post("/user").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Order(3)
	@Test
	void testUpdateUser() throws Exception {
		UserTO userTo = new UserTO();
		userTo.setId("673cb079ca3a4706c03db098");
		userTo.setUsername("user");
		userTo.setFullName("fullname");
		userTo.setAddress("address");
		userTo.setRole("CUSTOMER");

		var requestBody = mapper.writeValueAsString(userTo);

		when(userService.updateUser(Mockito.any(UserTO.class))).thenReturn(userTo);

		mockMvc.perform(
				MockMvcRequestBuilders.put("/user/1").contentType(MediaType.APPLICATION_JSON).content(requestBody))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	@Order(4)
	@Test
	void testDeleteUser() throws Exception {		
		mockMvc.perform(
				MockMvcRequestBuilders.delete("/user/1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Order(5)
	@Test
	void testGetRoles() throws Exception {
		when(userService.getRoles()).thenReturn(new ArrayList<Role>());
		mockMvc.perform(MockMvcRequestBuilders.get("/user/roles")).andExpect(MockMvcResultMatchers.status().isOk());
	}
}
