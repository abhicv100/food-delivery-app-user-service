package com.bits.pilani.user_service.controller;

import static com.bits.pilani.user_service.exception.CustomException.handleException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.to.ResponseTO;
import com.bits.pilani.user_service.to.SuccessResponseTO;
import com.bits.pilani.user_service.service.AuthService;
import com.bits.pilani.user_service.to.UsernamePasswordTO;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	AuthService authService;

	@GetMapping("/token")
	public ResponseEntity<ResponseTO> authenticateUser(@RequestBody UsernamePasswordTO usernamePasswordTO) {
		try  {
			authService.validateUsernamePasswordTO(usernamePasswordTO);
			String token = authService.authenticateAndGetToken(usernamePasswordTO);
			return SuccessResponseTO.create("Bearer " + token);
		} catch(CustomException e) {
			return handleException(e);
		}
	}
	
	@GetMapping("/authorize")
	public ResponseEntity<ResponseTO> authorizeToken(@RequestHeader(name = "Authorization", required = false) String authHeader) {		
		try  {			
			if(authHeader == null) {
				throw new CustomException(HttpStatus.BAD_REQUEST, "Authorization header is missing.");			
			}			
			if(authHeader.startsWith("Bearer")) {
				String token = authHeader.split(" ")[1];
				authService.validateJWTToken(token);
				return ResponseEntity.ok().build();
			}
			throw new CustomException(HttpStatus.BAD_REQUEST, "Bearer token expected in Authorization header.");
		} catch(CustomException e) {
			return handleException(e);
		}
	}
}
