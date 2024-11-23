package com.bits.pilani.user_service.controller;

import static com.bits.pilani.user_service.exception.CustomException.handleException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.security.Authorize;
import com.bits.pilani.user_service.to.ResponseTO;
import com.bits.pilani.user_service.to.SuccessResponseTO;
import com.bits.pilani.user_service.service.UserService;
import com.bits.pilani.user_service.to.UserTO;
import com.bits.pilani.user_service.util.TokenUtil;

@RestController
@ResponseBody
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
//	@Authorize
	@GetMapping("/{userId}")
	public ResponseEntity<ResponseTO> getUser(@PathVariable String userId, @RequestHeader("Authorization") String token) {
		try {
//			TokenUtil.validateUser(token, userId);
//			userService.checkIfUserIdExist(userId);
			var user = userService.getUser(userId);
			return SuccessResponseTO.create(user);
		} catch (CustomException e) {
			return handleException(e);
		}
	}

	@PostMapping
	public ResponseEntity<ResponseTO> createUser(@RequestBody UserTO userTO) {
		try {
			userService.validateUserTO(userTO);
			userTO.setId(null);
			var createdUser = userService.createUser(userTO);
			return SuccessResponseTO.create(createdUser, HttpStatus.CREATED);			
		} catch (CustomException e) {
			return handleException(e);
		}
	}

//	@Authorize
	@PutMapping("/{userId}")
	public ResponseEntity<ResponseTO> updateUser(@RequestBody UserTO userTO, @PathVariable String userId, @RequestHeader("Authorization") String token) {
		try {
//			TokenUtil.validateUser(token, userId);
//			userService.checkIfUserIdExist(userId);
			userService.validateUserTO(userTO);
			userTO.setId(userId);
			var updatedUser = userService.updateUser(userTO);
			return SuccessResponseTO.create(updatedUser);
		} catch (CustomException e) {
			return handleException(e);
		}
	}

//	@Authorize
	@DeleteMapping("/{userId}")
	public ResponseEntity<ResponseTO> deleteUser(@PathVariable String userId, @RequestHeader("Authorization") String token) {
		try {
//			TokenUtil.validateUser(token, userId);
//			userService.checkIfUserIdExist(userId);
			userService.deleteUser(userId);			
			return SuccessResponseTO.create(userId);
		} catch (CustomException e) {
			return handleException(e);
		}
	}
	
	@Authorize
	@GetMapping("/roles")
	public ResponseEntity<ResponseTO> getRoles() {
		var roles = userService.getRoles();
		return SuccessResponseTO.create(roles);			
	}
	
	@Authorize
	@GetMapping("/vehicleTypes")
	public ResponseEntity<ResponseTO> getVehicleTypes() {
		try {
			var vehicles = userService.getVehicleTypes();
			return SuccessResponseTO.create(vehicles);
		} catch (CustomException e) {
			return handleException(e);
		}
	}
}
