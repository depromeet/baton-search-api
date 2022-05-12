package depromeet.batonsearch.global.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionControllerAdvice {

    @ExceptionHandler(BindException.class)
    protected ResponseEntity<Map<String, String>> handleBindException(BindException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    protected ResponseEntity<Map<String, String>> handleSQLException(SQLIntegrityConstraintViolationException e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "sql error");
        return ResponseEntity.internalServerError().body(error);
    }
}
