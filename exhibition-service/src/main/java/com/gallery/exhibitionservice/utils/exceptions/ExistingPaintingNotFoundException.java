package com.gallery.exhibitionservice.utils.exceptions;

public class ExistingPaintingNotFoundException extends RuntimeException{
    public ExistingPaintingNotFoundException() {}

    public ExistingPaintingNotFoundException(String message) { super(message); }

    public ExistingPaintingNotFoundException(Throwable cause) { super(cause); }

    public ExistingPaintingNotFoundException(String message, Throwable cause) { super(message, cause); }
}
