package com.gallery.paintingservice.utils;

import com.gallery.paintingservice.utils.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ExistingPaintingNotFoundException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handlePaintingExhibitionNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ExistingPainterNotFoundException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handlePainterExhibitionNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY) //422 error status
    @ExceptionHandler(MinimumTitleLengthForPaintingNameException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleMinimumTitleLengthForPaintingNameException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY) //422 error status
    @ExceptionHandler(PaintingAlreadyExistsInGallery.class) //when this exception happen, this function would be called
    public HttpErrorInfo handlePaintingAlreadyExistsException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
