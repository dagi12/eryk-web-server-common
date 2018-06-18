package pl.edu.amu.wmi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Stworzone przez Eryk Mariankowski dnia 5.12.2017.
 */
@ControllerAdvice
public class BadRequestHandler {


    private static final Logger LOGGGER = LoggerFactory.getLogger(BadRequestHandler.class);

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handle(HttpMessageNotReadableException e) {
        LOGGGER.warn("Returning HTTP 400 Bad Request", e);
        throw e;
    }

}
