package com.gallery.exhibitionservice.utils;

import com.gallery.exhibitionservice.utils.exceptions.*;
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
    @ExceptionHandler(NotFoundException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ExistingExhibitionNotFoundException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleExistingExhibitionNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ExistingExhibitionNameException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleExistingExhibitionNameException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }
    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ExistingGalleryNotFoundException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleExistingGalleryNotFoundException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(NOT_FOUND, request, ex);
    }

    @ResponseStatus(UNPROCESSABLE_ENTITY) //422
    @ExceptionHandler(InvalidInputException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleInvalidInputException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
    }

    @ResponseStatus(BAD_REQUEST) //400
    @ExceptionHandler(PaintingAlreadyInExhibition.class) //when this exception happen, this function would be called
    public HttpErrorInfo handlePaintingAlreadyExistsException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(BAD_REQUEST) //400
    @ExceptionHandler(SculptureAlreadyInExhibition.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleSculptureAlreadyExistsException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(BAD_REQUEST) //400
    @ExceptionHandler(PaintingNotFromGalleryException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handlePaintingNotFromGalleryException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    @ResponseStatus(BAD_REQUEST) //400
    @ExceptionHandler(SculptureNotFromGalleryException.class) //when this exception happen, this function would be called
    public HttpErrorInfo handleSculptureNotFromGalleryException(WebRequest request, Exception ex) {
        return createHttpErrorInfo(BAD_REQUEST, request, ex);
    }

    private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, WebRequest request, Exception ex) {
        final String path = request.getDescription(false);
        final String message = ex.getMessage();

        log.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);

        return new HttpErrorInfo(httpStatus, path, message);
    }
}
