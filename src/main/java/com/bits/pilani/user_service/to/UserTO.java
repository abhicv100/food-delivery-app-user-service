package com.bits.pilani.user_service.to;

import com.bits.pilani.user_service.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class UserTO extends UserEntity {

}
