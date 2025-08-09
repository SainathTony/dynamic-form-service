package com.dynamic_form.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormBuilderService formBuilderService;

    public Map<String, Object> generateForm(String formInput) throws IOException {
        return this.formBuilderService.extractFormDetails(formInput);
    }
}
