package br.com.pupposoft.poc.springcloud.gateway.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.pupposoft.poc.springcloud.gateway.exception.ExceptionJson;
import br.com.pupposoft.poc.springcloud.gateway.exception.SystemBaseException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandlerConfiguration {

	@ExceptionHandler({ SystemBaseException.class})
	@ResponseBody
	public ResponseEntity<ExceptionJson> sasException(final SystemBaseException e, final HttpServletResponse response) {
		final ExceptionJson exceptionJson = new ExceptionJson(e);
		return new ResponseEntity<>(exceptionJson, new HttpHeaders(), e.getHttpStatus());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public ExceptionJson genericError(final Throwable e) {
		log.error(e.getMessage(), e);
		return new ExceptionJson(HttpStatus.INTERNAL_SERVER_ERROR.name(), e.getMessage());
	}

}
