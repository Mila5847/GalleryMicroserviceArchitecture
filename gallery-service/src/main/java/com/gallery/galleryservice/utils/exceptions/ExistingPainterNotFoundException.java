package com.gallery.galleryservice.utils.exceptions;

public class ExistingPainterNotFoundException extends RuntimeException {
    public ExistingPainterNotFoundException() {}

    public ExistingPainterNotFoundException(String message) { super(message); }

    public ExistingPainterNotFoundException(Throwable cause) { super(cause); }

    public ExistingPainterNotFoundException(String message, Throwable cause) { super(message, cause); }
}
