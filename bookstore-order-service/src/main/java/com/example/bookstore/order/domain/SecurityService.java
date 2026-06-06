package com.example.bookstore.order.domain;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SecurityService {

    public String getCurrentUser() {
    	
    	JwtAuthenticationToken jwtAuthentication = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    	Jwt jwt = (Jwt) jwtAuthentication.getPrincipal();
    	log.info("Current Username: " + jwt.getClaimAsString("preferred_username"));
    	return jwt.getClaimAsString("preferred_username");
    }
}