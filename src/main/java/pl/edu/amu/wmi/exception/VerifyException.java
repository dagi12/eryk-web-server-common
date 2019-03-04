package pl.edu.amu.wmi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by erykmariankowski on 04.03.2019.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class VerifyException extends RuntimeException {
    public VerifyException(String result) {
        super(result);
    }
}
