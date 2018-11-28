package hello;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class MeasurementNotFoundAdvice {

	@ResponseBody
	@ExceptionHandler(MeasurementNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String measurementNotFoundHandler(MeasurementNotFoundException ex) {
		return ex.getMessage();
	}
}
