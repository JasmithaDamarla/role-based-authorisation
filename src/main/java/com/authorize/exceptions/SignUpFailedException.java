package com.authorize.exceptions;

public class SignUpFailedException extends RuntimeException {
	public SignUpFailedException(String msg) {
		super(msg);
	}
}