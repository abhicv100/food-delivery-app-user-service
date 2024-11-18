package com.bits.pilani.user_service.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.security.Role;
import com.bits.pilani.user_service.dao.UserDao;
import com.bits.pilani.user_service.dao.VehicleTypeDao;
import com.bits.pilani.user_service.entity.UserEntity;
import com.bits.pilani.user_service.entity.VehicleTypeEntity;
import com.bits.pilani.user_service.to.UserTO;

@SpringBootTest
public class UserServiceTest {

	@MockBean
	UserDao userDao;
		
	@MockBean
	VehicleTypeDao vehicleTypeDao;
	
	@Autowired
	UserService userService;
	
	@Order(1)
	@Test
	void testGetUser_Success() throws CustomException {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1);
		userEntity.setPassword("1234");
		when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(userEntity));
		var user = userService.getUser(1);
		assertEquals(user.getId(), 1);
		assertNull(user.getPassword());
	}

	@Order(2)
	@Test
	void testGetUser_CustomException() throws CustomException {
		when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.getUser(1);
        });
		assertEquals("User id = '1' is invalid", exception.getMessage());
	}
	
	@Order(3)
	@Test
	void testCreateUser() throws CustomException {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1);
		userEntity.setPassword("1234");
		
		when(userDao.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
		
		UserTO mockUserTO = new UserTO();
		mockUserTO.setId(null);
		mockUserTO.setPassword("1234");
		
		var user = userService.createUser(mockUserTO);
		assertEquals(user.getId(), 1);
		assertNull(user.getPassword());
	}

	@Order(4)
	@Test
	void testUpdateUser() throws CustomException {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(1);
		userEntity.setPassword("1234");
		
		when(userDao.save(Mockito.any(UserEntity.class))).thenReturn(userEntity);
		
		UserTO mockUserTO = new UserTO();
		mockUserTO.setId(1);
		mockUserTO.setPassword("1234");
		
		var user = userService.updateUser(mockUserTO);
		assertEquals(user.getId(), 1);
		assertNull(user.getPassword());
	}
	
	@Order(5)
	@Test
	void testGetVehicleTypes() throws CustomException {
		VehicleTypeEntity entity = new VehicleTypeEntity();
		entity.setId(1);
		entity.setName("two-wheeler");
		
		when(vehicleTypeDao.findAll()).thenReturn(List.of(entity));
		
		var vehicleTOs = userService.getVehicleTypes();
		
		assertNotNull(vehicleTOs);
		assertEquals(vehicleTOs.size(), 1);
		assertEquals(vehicleTOs.get(0).getId(), entity.getId());
		assertEquals(vehicleTOs.get(0).getName(), entity.getName());		
	}
	
	@Order(6)
	@Test
	public void testCheckIfUserIdExist_Success() throws CustomException {
		int id = 0;
		when(userDao.existsById(Mockito.anyInt())).thenReturn(true);
		assertDoesNotThrow(() -> {
        	userService.checkIfUserIdExist(id);
        });
	}
	
	@Order(7)
	@Test
	public void testCheckIfUserIdExist_Failure() throws CustomException {
		int id = 0;
		when(userDao.existsById(Mockito.anyInt())).thenReturn(false);
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.checkIfUserIdExist(id);
        });
		assertEquals(String.format("User id = '%d' is invalid", id), exception.getMessage());
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
		userTO.setRoleId(-1);
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assertEquals("Role id  = '-1' is invalid.", exception.getMessage());
	}
	
	@Order(10)
	@Test
	void testValidateUserTO_CustomerMissingAddress() {
		UserTO userTO = new UserTO();
		userTO.setUsername("user");
		userTO.setPassword("password");
		userTO.setFullName("fullname");
		userTO.setRoleId(Role.CUSTOMER.ordinal() + 1);		
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assertEquals("Address is missing. Please provide address.", exception.getMessage());
	}
	
	@Order(11)
	@Test
	void testValidateUserTO_DeilveryPersonalMissingVehicleType() {
		UserTO userTO = new UserTO();
		userTO.setUsername("user");
		userTO.setPassword("password");
		userTO.setFullName("fullname");
		userTO.setRoleId(Role.DELIVERY_PERSONNEL.ordinal() + 1);		
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assertEquals("Vehicle type id is missing. Please provide vehicle type id", exception.getMessage());
	}
	
	@Order(12)
	@Test
	void testValidateUserTO_DeilveryPersonalInvalidVehicleType() {
		UserTO userTO = new UserTO();
		userTO.setUsername("user");
		userTO.setPassword("password");
		userTO.setFullName("fullname");
		userTO.setRoleId(Role.DELIVERY_PERSONNEL.ordinal() + 1);		
		userTO.setVehicleTypeId(-1);
		CustomException exception = assertThrows(CustomException.class, () -> {
        	userService.validateUserTO(userTO);
        });
		assertEquals("Vehicle type id = '-1' is invalid", exception.getMessage());
	}
}
