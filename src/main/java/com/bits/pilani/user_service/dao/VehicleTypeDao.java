package com.bits.pilani.user_service.dao;

import org.springframework.data.repository.ListCrudRepository;

import com.bits.pilani.user_service.entity.VehicleTypeEntity;

public interface VehicleTypeDao extends ListCrudRepository<VehicleTypeEntity, Integer>  {

}
