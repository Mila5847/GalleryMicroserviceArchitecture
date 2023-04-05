package com.gallery.exhibitionservice.utils.exceptions;

public class ExistingGalleryNotFoundException extends RuntimeException{
    public ExistingGalleryNotFoundException() {}

    public ExistingGalleryNotFoundException(String message) { super(message); }

    public ExistingGalleryNotFoundException(Throwable cause) { super(cause); }

    public ExistingGalleryNotFoundException(String message, Throwable cause) { super(message, cause); }
}
