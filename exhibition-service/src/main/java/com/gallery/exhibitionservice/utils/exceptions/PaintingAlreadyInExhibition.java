package com.gallery.exhibitionservice.utils.exceptions;

public class PaintingAlreadyInExhibition extends RuntimeException{
    public PaintingAlreadyInExhibition() {}

    public PaintingAlreadyInExhibition(String message) { super(message); }

    public PaintingAlreadyInExhibition(Throwable cause) { super(cause); }

    public PaintingAlreadyInExhibition(String message, Throwable cause) { super(message, cause); }
}
