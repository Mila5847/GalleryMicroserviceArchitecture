package com.gallery.exhibitionservice.utils.exceptions;

public class SculptureAlreadyInExhibition extends RuntimeException{
    public SculptureAlreadyInExhibition() {}

    public SculptureAlreadyInExhibition(String message) { super(message); }

    public SculptureAlreadyInExhibition(Throwable cause) { super(cause); }

    public SculptureAlreadyInExhibition(String message, Throwable cause) { super(message, cause); }
}
