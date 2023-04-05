package com.gallery.paintingservice.utils.exceptions;

public class NameMissingFromQueryParamsException extends RuntimeException{
    public NameMissingFromQueryParamsException() {}

    public NameMissingFromQueryParamsException(String message) { super(message); }

    public NameMissingFromQueryParamsException(Throwable cause) { super(cause); }

    public NameMissingFromQueryParamsException(String message, Throwable cause) { super(message, cause); }
}
