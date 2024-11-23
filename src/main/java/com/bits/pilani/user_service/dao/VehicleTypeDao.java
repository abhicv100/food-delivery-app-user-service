package com.bits.pilani.user_service.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bits.pilani.user_service.entity.VehicleTypeEntity;

public interface VehicleTypeDao extends MongoRepository<VehicleTypeEntity, Integer>  {

}
