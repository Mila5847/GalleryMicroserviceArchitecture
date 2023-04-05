package com.gallery.paintingservice.utils.exceptions;

public class ExistingExhibitionNotFoundException extends RuntimeException {
    public ExistingExhibitionNotFoundException() {}

    public ExistingExhibitionNotFoundException(String message) { super(message); }

    public ExistingExhibitionNotFoundException(Throwable cause) { super(cause); }

    public ExistingExhibitionNotFoundException(String message, Throwable cause) { super(message, cause); }
}
