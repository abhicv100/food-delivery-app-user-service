package com.bits.pilani.user_service.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bits.pilani.user_service.entity.UserEntity;

public interface UserDao extends MongoRepository<UserEntity, String> {
	UserEntity findByUsername(String username);
}
