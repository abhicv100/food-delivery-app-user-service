package com.bits.pilani.user_service.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.bits.pilani.user_service.entity.UserEntity;

public interface UserDao extends ListCrudRepository<UserEntity, Integer> {
	UserEntity findByUsername(String username);
}
