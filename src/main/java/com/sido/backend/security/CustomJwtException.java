package com.sido.backend.security;

public class CustomJwtException extends RuntimeException{
	public CustomJwtException(String message){
		super("Jwt Exception: " + message);
	}
}
