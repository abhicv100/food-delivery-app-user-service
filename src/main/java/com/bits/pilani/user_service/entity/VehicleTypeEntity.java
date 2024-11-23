package com.bits.pilani.user_service.entity;

import org.springframework.data.annotation.Id;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VehicleTypeEntity {
	
	@Id
	Integer id;
	
	String name;
}
