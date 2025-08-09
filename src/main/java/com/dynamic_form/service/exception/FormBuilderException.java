package com.dynamic_form.service.exception;

public class FormBuilderException extends RuntimeException {
    public FormBuilderException(String message) {
        super(message);
    }
    
    public FormBuilderException(String message, Throwable cause) {
        super(message, cause);
    }
}