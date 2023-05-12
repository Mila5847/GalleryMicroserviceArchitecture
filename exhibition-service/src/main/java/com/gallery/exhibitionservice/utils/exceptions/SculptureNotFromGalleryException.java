package com.gallery.exhibitionservice.utils.exceptions;

public class SculptureNotFromGalleryException extends RuntimeException{
    public SculptureNotFromGalleryException() {}

    public SculptureNotFromGalleryException(String message) { super(message); }

    public SculptureNotFromGalleryException(Throwable cause) { super(cause); }

    public SculptureNotFromGalleryException(String message, Throwable cause) { super(message, cause); }
}
