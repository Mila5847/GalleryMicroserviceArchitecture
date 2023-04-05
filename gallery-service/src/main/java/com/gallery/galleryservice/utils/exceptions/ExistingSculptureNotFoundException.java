package com.gallery.galleryservice.utils.exceptions;

public class ExistingSculptureNotFoundException extends RuntimeException{
    public ExistingSculptureNotFoundException() {}

    public ExistingSculptureNotFoundException(String message) { super(message); }

    public ExistingSculptureNotFoundException(Throwable cause) { super(cause); }

    public ExistingSculptureNotFoundException(String message, Throwable cause) { super(message, cause); }
}
