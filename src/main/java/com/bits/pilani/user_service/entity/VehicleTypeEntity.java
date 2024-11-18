package com.bits.pilani.user_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Table(name = "VehicleType", schema = "public")
@Entity
@Getter @Setter
public class VehicleTypeEntity {

	@Id
	@Column
	Integer id;
	
	@Column
	String name;
}
