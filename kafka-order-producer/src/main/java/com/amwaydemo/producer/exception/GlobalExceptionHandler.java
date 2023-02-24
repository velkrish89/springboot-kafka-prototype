package com.amwaydemo.producer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(NoOrderFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ResponseBody
	public ErrorResponse handleNoOrderFoundException(NoOrderFoundException ex) {
		ErrorResponse response = new ErrorResponse();
		response.setMessage("No records found");
		return response;
	}
	
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorResponse handleDefaultException(Exception ex) {
	    ErrorResponse response = new ErrorResponse();
	    response.setMessage(ex.getMessage());
	    return response;
	}
	

}
