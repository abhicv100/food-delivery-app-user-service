package com.bits.pilani.user_service.config;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
@EnableWebMvc
public class GlobalWebConfig implements WebMvcConfigurer {
		
	static final String secret = "gZcLm+oqbX2jqZTiSt/LmdFsDZItipAMM3PYRMc4kJs=";

	// invalid token eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmhpY3YiLCJpYXQiOjE3MzE0MjE2MDksInVzZXJJZCI6MSwicm9sZSI6ImN1c3RvbWVyIn0.2U41HbQ5i1Pk_0zbD3FStme_mueIatB86WA_hEpRFyw

	@Bean
	public static SecretKey getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secret);
		SecretKey key = Keys.hmacShaKeyFor(keyBytes);
		return key;
	}
}
