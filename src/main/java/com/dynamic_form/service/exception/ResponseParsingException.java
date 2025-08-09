package com.dynamic_form.service.exception;

public class ResponseParsingException extends FormBuilderException {
    public ResponseParsingException(String message) {
        super(message);
    }
    
    public ResponseParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}