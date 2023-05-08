package com.example.demo.exceptions.hadler;



import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.exceptions.ExpectionResponse;
import com.example.demo.exceptions.NotFoundExpection;

@RestController
@ControllerAdvice
public class CustomResponseEntityExpectionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExpectionResponse> handleAllException(Exception ex,
			WebRequest request){
		ExpectionResponse expectionResponse = new ExpectionResponse(
				new Date(),
				ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(expectionResponse,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ExceptionHandler(NotFoundExpection.class)
	public final ResponseEntity<ExpectionResponse> handleNotFoundExceptions(Exception ex,
			WebRequest request){
		ExpectionResponse expectionResponse = new ExpectionResponse(
				new Date(),
				ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(expectionResponse,HttpStatus.NOT_FOUND);
	}
}
