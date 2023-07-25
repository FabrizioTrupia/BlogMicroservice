package it.cgmconsulting.mscategory.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionManagement {
/*
	@ExceptionHandler({IllegalArgumentException.class})
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex){
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
*/

	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex){
		return new ResponseEntity<String>("Data non valida", HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({MissingServletRequestPartException.class})
	public ResponseEntity<String> handleMissingServletRequestPartException(MissingServletRequestPartException ex){
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ResourceNotFoundException.class})
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.NOT_FOUND);
	}


	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<String> handleArgumentNotValidValue(MethodArgumentNotValidException ex) {

		BindingResult bindingResults = ex.getBindingResult();
		List<String> errors = bindingResults
				.getFieldErrors()
				.stream().map(e -> {
					return e.getField()+": "+e.getDefaultMessage();
				})
				.collect(Collectors.toList());

		return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {

		Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
		List<String> errors = violations
				.stream().map(e -> {
					return e.getMessage();
				})
				.collect(Collectors.toList());

		return new ResponseEntity<String>(errors.toString(), HttpStatus.BAD_REQUEST);
	}
	
}
