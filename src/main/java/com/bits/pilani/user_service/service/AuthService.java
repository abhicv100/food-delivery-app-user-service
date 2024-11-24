package com.bits.pilani.user_service.service;

import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bits.pilani.user_service.config.GlobalWebConfig;
import com.bits.pilani.user_service.dao.UserDao;
import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.security.Role;
import com.bits.pilani.user_service.to.UsernamePasswordTO;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

@Service
public class AuthService {

	@Autowired
	UserDao userDao;

	@Autowired
	SecretKey secretKey;

	public String authenticateAndGetToken(UsernamePasswordTO usernamePasswordTO) throws CustomException {

		var userEntity = userDao.findByUsername(usernamePasswordTO.getUsername());

		if (userEntity == null) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Username or password is invalid.");
		}

		if (!userEntity.getPassword().equals(usernamePasswordTO.getPassword())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Username or password is invalid.");
		}

		try {
			String token = Jwts.builder().subject(usernamePasswordTO.getUsername())
					.issuedAt(new Date(System.currentTimeMillis())).claim("userId", userEntity.getId())
					.claim("role", userEntity.getRole()).signWith(secretKey).compact();
			return token;
		} catch (Exception e) {
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}
	
	public void validateJWTToken(String token) throws CustomException {
		try {
			var claims = Jwts.parser().verifyWith(GlobalWebConfig.getSignInKey()).build().parseSignedClaims(token).getPayload();					
			if(!claims.containsKey("role")) {
				throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid token claim.");				
			}
			String roleName = String.class.cast(claims.get("role"));
			Role.valueOf(roleName);
		} catch (JwtException e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid bearer token.");
		} catch(IllegalArgumentException e) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Invalid token claim.");			
		}
	}

	public void validateUsernamePasswordTO(UsernamePasswordTO usernamePasswordTO) throws CustomException {

		if (Objects.isNull(usernamePasswordTO.getUsername())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Username is missing. Please provide username.");
		}

		if (Objects.isNull(usernamePasswordTO.getPassword())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Password is missing. Please provide password.");
		}
	}
}
