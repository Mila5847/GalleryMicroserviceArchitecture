package com.gallery.paintingservice.utils.exceptions;

public class PaintingAlreadyExistsInGallery extends RuntimeException{
    public PaintingAlreadyExistsInGallery() {}

    public PaintingAlreadyExistsInGallery(String message) { super(message); }

    public PaintingAlreadyExistsInGallery(Throwable cause) { super(cause); }

    public PaintingAlreadyExistsInGallery(String message, Throwable cause) { super(message, cause); }
}
