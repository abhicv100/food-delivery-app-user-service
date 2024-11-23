//package com.bits.pilani.user_service.service;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import javax.crypto.SecretKey;
//
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import com.bits.pilani.user_service.exception.CustomException;
//import com.bits.pilani.user_service.security.Role;
//import com.bits.pilani.user_service.dao.UserDao;
//import com.bits.pilani.user_service.entity.UserEntity;
//import com.bits.pilani.user_service.to.UsernamePasswordTO;
//
//import io.jsonwebtoken.Jwts;
//
//@SpringBootTest
//public class AuthServiceTest {
//	
//	@Autowired
//	AuthService authService;
//	
//	@MockBean
//	UserDao userDao;
//	
//	@Autowired
//	SecretKey secretKey;
//	
//	@Order(1)
//	@Test
//	void testAuthenticateAndGetToken_InvalidUsername() {
//		UsernamePasswordTO usernamePasswordTO = new UsernamePasswordTO();
//		usernamePasswordTO.setUsername("user");
//		when(userDao.findByUsername(Mockito.anyString())).thenReturn(null);
//		
//		CustomException exception = assertThrows(CustomException.class, () -> {
//			authService.authenticateAndGetToken(usernamePasswordTO);
//        });
//		
//		assertEquals("Username = 'user' is invalid", exception.getMessage());		
//	}
//	
//	@Order(2)
//	@Test
//	void testAuthenticateAndGetToken_InvalidPassword() {
//		UsernamePasswordTO usernamePasswordTO = new UsernamePasswordTO();
//		usernamePasswordTO.setUsername("user");
//		usernamePasswordTO.setPassword("password");
//		
//		UserEntity user = new UserEntity();
//		user.setUsername("user");
//		user.setPassword("1234");
//		
//		when(userDao.findByUsername(Mockito.anyString())).thenReturn(user);
//		
//		CustomException exception = assertThrows(CustomException.class, () -> {
//			authService.authenticateAndGetToken(usernamePasswordTO);
//        });
//		
//		assertEquals("Password is invalid", exception.getMessage());		
//	}
//	
//	@Order(3)
//	@Test
//	void testAuthenticateAndGetToken_Success() throws CustomException {
//		UsernamePasswordTO usernamePasswordTO = new UsernamePasswordTO();
//		usernamePasswordTO.setUsername("user");
//		usernamePasswordTO.setPassword("1234");
//		
//		UserEntity user = new UserEntity();
//		user.setUsername("user");
//		user.setPassword("1234");
//		user.setId(1);
//		
//		user.setRoleId(Role.CUSTOMER.ordinal() + 1);
//		
//		when(userDao.findByUsername(Mockito.anyString())).thenReturn(user);
//		
//		String token = authService.authenticateAndGetToken(usernamePasswordTO);
//		
//		var claims = Jwts.parser().verifyWith(secretKey).build()
//							.parseSignedClaims(token).getPayload();			
//		
//		assertEquals(claims.get("role"), Role.CUSTOMER.name());
//		assertEquals(claims.get("userId"), 1);
//	}
//	
//	@Order(4)
//	@Test
//	void validateUsernamePasswordTO_MissingUsername() {
//		UsernamePasswordTO usernamePasswordTO = new UsernamePasswordTO();
//		usernamePasswordTO.setPassword("1234");
//		
//		CustomException exception = assertThrows(CustomException.class, () -> {
//			authService.validateUsernamePasswordTO(usernamePasswordTO);
//        });
//		
//		assertEquals("Username is missing. Please provide username.", exception.getMessage());
//	}
//	
//	@Order(5)
//	@Test
//	void validateUsernamePasswordTO_MissingPassword() {
//		UsernamePasswordTO usernamePasswordTO = new UsernamePasswordTO();
//		usernamePasswordTO.setUsername("user");
//		
//		CustomException exception = assertThrows(CustomException.class, () -> {
//			authService.validateUsernamePasswordTO(usernamePasswordTO);
//        });
//		
//		assertEquals("Password is missing. Please provide password.", exception.getMessage());
//	}
//
//}
