package com.bits.pilani.user_service.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document("user")
@Getter @Setter
public class UserEntity {
	
	@Id
	String id;
	
	String username;
	
	String password;
	
	String fullName;
	
	String address;
	
	String phone;
	
	String email;
		
	String role;
}
