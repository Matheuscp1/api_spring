package com.example.demo.exceptions.hadle;



import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.demo.exceptions.ExpectionResponse;

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
		return new ResponseEntity<>(expectionResponse,HttpStatus.BAD_REQUEST);
	}
}
