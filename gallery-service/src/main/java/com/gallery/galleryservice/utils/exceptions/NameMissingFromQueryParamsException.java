package com.gallery.galleryservice.utils.exceptions;

public class NameMissingFromQueryParamsException extends RuntimeException {
    public NameMissingFromQueryParamsException(String message) {
        super(message);
    }
}
