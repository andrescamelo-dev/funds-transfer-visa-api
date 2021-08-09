package com.visa.fundstransfer.exception;

import com.visa.fundstransfer.common.ExceptionTypesEnum;
import com.visa.fundstransfer.common.Utils;
import com.visa.fundstransfer.domain.dto.ErrorResponseDTO;
import com.visa.fundstransfer.service.TransferService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	static final Logger LOG = LoggerFactory.getLogger(TransferService.class);

	@org.springframework.web.bind.annotation.ExceptionHandler({ IllegalArgumentException.class,
			IllegalStateException.class })
	public ResponseEntity<Object> handleMethodInternalError(Exception ex, WebRequest request) {
		ErrorResponseDTO errors = Utils.errorResponse(ExceptionTypesEnum.SEVERAL_ERROR.getCode(),ExceptionTypesEnum.SEVERAL_ERROR.getDescription());
		LOG.error("SEVERAL ERROR", ex);

		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
	}
}
