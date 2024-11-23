package com.bits.pilani.user_service.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.bits.pilani.user_service.exception.CustomException;
import com.bits.pilani.user_service.security.Role;
import com.bits.pilani.user_service.dao.UserDao;
import com.bits.pilani.user_service.dao.VehicleTypeDao;
import com.bits.pilani.user_service.entity.UserEntity;
import com.bits.pilani.user_service.to.UserTO;
import com.bits.pilani.user_service.to.VehicleTypeTO;

@Service
public class UserService {

	@Autowired
	UserDao userDao;

	@Autowired
	VehicleTypeDao vehicleTypeDao;

	public UserTO getUser(String userId) throws CustomException {

		try {
			UserTO userTO = new UserTO();

			Optional<UserEntity> maybeUserEntity = userDao.findById(userId);

			if (maybeUserEntity.isPresent()) {
				BeanUtils.copyProperties(maybeUserEntity.get(), userTO);
			} else {
				String errorMsg = String.format("User id = '%s' is invalid", userId);
				throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
			}

			userTO.setPassword(null);

			return userTO;
		} catch (DataAccessException e) {
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public UserTO createUser(UserTO userTO) throws CustomException {
		try {
			UserEntity userEntity = new UserEntity();
			BeanUtils.copyProperties(userTO, userEntity);
			userEntity = userDao.save(userEntity);
			BeanUtils.copyProperties(userEntity, userTO);
			userTO.setPassword(null);
			return userTO;
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public UserTO updateUser(UserTO userTO) throws CustomException {
		return createUser(userTO);
	}

	public void deleteUser(String userId) throws CustomException {
		try {
			userDao.deleteById(userId);
		} catch (DataAccessException e) {
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public List<Role> getRoles() {
		return Arrays.asList(Role.values());
	}

	public List<VehicleTypeTO> getVehicleTypes() throws CustomException {
		try {
			return vehicleTypeDao.findAll().stream().map((vehicleTypeEntity) -> {
				System.out.println(vehicleTypeEntity);
				VehicleTypeTO vehicleTypeTO = new VehicleTypeTO();
				BeanUtils.copyProperties(vehicleTypeEntity, vehicleTypeTO);
				return vehicleTypeTO;
			}).toList();
		} catch (DataAccessException e) {
			throw CustomException.INTERNAL_SERVER_ERRROR;
		}
	}

	public void checkIfUserIdExist(String userId) throws CustomException {
		if (!userDao.existsById(userId)) {
			String errorMsg = String.format("User id = '%s' is invalid", userId);
			throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
		}
	}

	public void validateUserTO(UserTO userTo) throws CustomException {

		if (Objects.isNull(userTo.getUsername())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Username is missing. Please provide username.");
		}

		if (Objects.isNull(userTo.getPassword())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Password is missing. Please provide password.");
		}

		if (Objects.isNull(userTo.getFullName())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Fullname is missing. Please provide fullname.");
		}

		if (Objects.isNull(userTo.getRole())) {
			throw new CustomException(HttpStatus.BAD_REQUEST, "Role is missing. Please provide role.");
		}

		try {
			var role = Role.valueOf(userTo.getRole());
			if (role.equals(Role.CUSTOMER)) {
				if (Objects.isNull(userTo.getAddress())) {
					throw new CustomException(HttpStatus.BAD_REQUEST, "Address is missing. Please provide address.");
				}
			}
		} catch (IllegalArgumentException e) {
			var supportedRoles = Arrays.asList(Role.values()).stream().map((role) -> role.name())
					.collect(Collectors.joining(", "));
			var errorMsg = String.format("Invalid role = '%s'. Supported roles: [%s]", userTo.getRole(),
					supportedRoles);
			throw new CustomException(HttpStatus.BAD_REQUEST, errorMsg);
		}
	}
}
