package org.group4.dvdshopbackend.core.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.group4.dvdshopbackend.core.api.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ApiError handleAllExceptions(HttpServletRequest req, Exception e) throws Exception {

        String uri = req.getRequestURI();
        if (uri.startsWith("/v3/api-docs") || uri.startsWith("/swagger-ui")) {
            throw e;
        }

        var status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        var message = e.getMessage();

        return new ApiError(status, message);
    }
}