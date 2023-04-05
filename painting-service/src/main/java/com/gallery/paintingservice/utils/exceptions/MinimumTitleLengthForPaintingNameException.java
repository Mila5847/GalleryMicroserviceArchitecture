package com.gallery.paintingservice.utils.exceptions;

public class MinimumTitleLengthForPaintingNameException extends RuntimeException{
    public MinimumTitleLengthForPaintingNameException() {}

    public MinimumTitleLengthForPaintingNameException(String message) { super(message); }

    public MinimumTitleLengthForPaintingNameException(Throwable cause) { super(cause); }

    public MinimumTitleLengthForPaintingNameException(String message, Throwable cause) { super(message, cause); }
}
