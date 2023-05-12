package com.gallery.exhibitionservice.utils.exceptions;

public class PaintingNotFromGalleryException extends RuntimeException{
    public PaintingNotFromGalleryException() {}

    public PaintingNotFromGalleryException(String message) { super(message); }

    public PaintingNotFromGalleryException(Throwable cause) { super(cause); }

    public PaintingNotFromGalleryException(String message, Throwable cause) { super(message, cause); }
}
