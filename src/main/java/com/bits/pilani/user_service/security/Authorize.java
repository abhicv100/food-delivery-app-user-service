package com.bits.pilani.user_service.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {
	
	Role [] roles() default {Role.ADMIN, Role.CUSTOMER, Role.DELIVERY_PERSONNEL, Role.RESTAURANT_OWNER};
}
