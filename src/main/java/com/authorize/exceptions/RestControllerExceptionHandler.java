package com.authorize.exceptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestControllerAdvice
public class RestControllerExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value=HttpStatus.BAD_REQUEST)
	public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException func, WebRequest req) {
		List<String> inpErrors = new ArrayList<>();
		func.getAllErrors()
			.forEach(error -> 
				inpErrors.add(error.getDefaultMessage()));
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(), inpErrors.toString(), req.getDescription(false));
		log.info("Method arugument not valid exception raised : {}",inpErrors.toString());
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(UnAuthorizedException.class)
	public ResponseEntity<ExceptionResponse> handleUserException(UnAuthorizedException userException, WebRequest req) {
		log.info("Unauthorised exception has been raised : {}",userException.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),userException.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ExceptionResponse> handleUserException(UserNotFoundException userException, WebRequest req) {
		log.info("UserNotFoundException exception has been raised : {}",userException.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),userException.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(SignUpFailedException.class)
	public ResponseEntity<ExceptionResponse> handleTraineeException(SignUpFailedException signupfail, WebRequest req) {
		log.info("signupfailed exception has been raised : {}",signupfail.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),signupfail.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException runtime, WebRequest req) {
		log.info("Run time exception has been raised : {}",runtime.getMessage());
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date().toString(),
				HttpStatus.BAD_REQUEST.name(),runtime.getMessage(), req.getDescription(false));
		return new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
