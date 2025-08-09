package com.dynamic_form.service.exception;

import lombok.Getter;

@Getter
public class ApiException extends FormBuilderException {
    private final int statusCode;
    
    public ApiException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}