package com.bits.pilani.user_service.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bits.pilani.user_service.dao.UserDao;
import com.bits.pilani.user_service.entity.UserEntity;
import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.security.Role;
import com.bits.pilani.user_service.to.UserTO;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	UserDao userDao;
	
	@Autowired
	UserService userService;
	
	@Order(1)
	@Test
	void testGetUser_Success() throws CustomException {
		UserEntity userEntity = new UserEntity();
		userEntity.setId("673cb079ca3a4706c03db098");
		userEntity.setPassword("1234");
		when(userDao.findById(Mockito.anyString())).thenReturn(Optional.of(userEntity));
		var user = userService.getUser("673cb079ca3a4706c03db098");
		assertEquals(user.getId(), "673cb079ca3a4706c03db098");
		assertNull(user.getPassword());
	}

	@Order(2)
	@Test
	void testGetUser_CustomException() throws CustomException {
		when(userDao.findById(Mockito.anyString())).thenReturn(Optional.empty());
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.getUser("673cb079ca3a4706c03db098");
        });
		assertEquals("User id = '673cb079ca3a4706c03db098' is invalid", exception.getMessage());
	}
	
	@Order(3)
	@Test
	void testCreateUser() throws CustomException {
		UserEntity userEntity = new UserEntity();
		userEntity.setId("673cb079ca3a4706c03db098");
		userEntity.setPassword("1234");
		
		when(userDao.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
		
		UserTO mockUserTO = new UserTO();
		mockUserTO.setId(null);
		mockUserTO.setPassword("1234");
		
		var user = userService.createUser(mockUserTO);
		assertEquals(user.getId(), "673cb079ca3a4706c03db098");
		assertNull(user.getPassword());
	}

	@Order(4)
	@Test
	void testUpdateUser() throws CustomException {
		UserEntity userEntity = new UserEntity();
		userEntity.setId("673cb079ca3a4706c03db098");
		userEntity.setPassword("1234");
		
		when(userDao.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
		
		UserTO mockUserTO = new UserTO();
		mockUserTO.setId("673cb079ca3a4706c03db098");
		mockUserTO.setPassword("1234");
		
		var user = userService.updateUser(mockUserTO);
		assertEquals(user.getId(), "673cb079ca3a4706c03db098");
		assertNull(user.getPassword());
	}
	
	@Order(6)
	@Test
	public void testCheckIfUserIdExist_Success() throws CustomException {
		when(userDao.existsById(Mockito.anyString())).thenReturn(true);
		assertDoesNotThrow(() -> {
        	userService.checkIfUserIdExist("673cb079ca3a4706c03db098");
        });
	}
	
	@Order(7)
	@Test
	public void testCheckIfUserIdExist_Failure() throws CustomException {
		String id = "673cb079ca3a4706c03db098";
		when(userDao.existsById(Mockito.anyString())).thenReturn(false);
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.checkIfUserIdExist("673cb079ca3a4706c03db098");
        });
		assertEquals(String.format("User id = '%s' is invalid", id), exception.getMessage());
	}
	
	@Order(8)
	@Test
	void testValidateUserTO_MissingRequiredProperty() {
		UserTO userTO = new UserTO();
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assertEquals("Username is missing. Please provide username.", exception.getMessage());
	}
	
	@Order(9)
	@Test
	void testValidateUserTO_InvalidRoleId() {
		UserTO userTO = new UserTO();
		userTO.setUsername("user");
		userTO.setPassword("password");
		userTO.setFullName("fullname");
		userTO.setRole("invalid_role");
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assert(exception.getMessage().contains("Invalid role"));
	}
	
	@Order(10)
	@Test
	void testValidateUserTO_CustomerMissingAddress() {
		UserTO userTO = new UserTO();
		userTO.setUsername("user");
		userTO.setPassword("password");
		userTO.setFullName("fullname");
		userTO.setRole(Role.CUSTOMER.name());		
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assertEquals("Address is missing. Please provide address.", exception.getMessage());
	}
}
