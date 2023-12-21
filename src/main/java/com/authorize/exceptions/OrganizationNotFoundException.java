package com.authorize.exceptions;

public class OrganizationNotFoundException extends RuntimeException{
	public OrganizationNotFoundException(String msg) {
		super(msg);
	}
}
