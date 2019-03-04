package pl.edu.amu.wmi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.edu.amu.wmi.model.GeneralResponse;

@ControllerAdvice
@RestController
public class VerifyExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("unused")
    @ExceptionHandler(VerifyException.class)
    public final ResponseEntity<GeneralResponse> handleUserNotFoundException(VerifyException ex,
                                                                             WebRequest request) {
        GeneralResponse errorDetails = new GeneralResponse<>(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

}