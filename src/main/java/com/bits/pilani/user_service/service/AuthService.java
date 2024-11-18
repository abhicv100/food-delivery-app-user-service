package com.bits.pilani.user_service.service;

import java.util.Date;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bits.pilani.user_service.dao.UserDao;
import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.security.Role;
import com.bits.pilani.user_service.to.UsernamePasswordTO;

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
			String errorMsg = String.format("Username = '%s' is invalid", usernamePasswordTO.getUsername());
			throw new CustomException(HttpStatus.UNAUTHORIZED, errorMsg);
		}

		if (!userEntity.getPassword().equals(usernamePasswordTO.getPassword())) {
			throw new CustomException(HttpStatus.UNAUTHORIZED, "Password is invalid");
		}

		var mayBeRole = Role.findById(userEntity.getRoleId());

		if (mayBeRole.isPresent()) {
			try {
				String token = Jwts.builder().subject(usernamePasswordTO.getUsername())
						.issuedAt(new Date(System.currentTimeMillis())).claim("userId", userEntity.getId())
						.claim("role", mayBeRole.get().name()).signWith(secretKey).compact();
				return token;
			} catch (Exception e) {
				throw CustomException.INTERNAL_SERVER_ERRROR;
			}
		} else {
			throw CustomException.INTERNAL_SERVER_ERRROR;
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
