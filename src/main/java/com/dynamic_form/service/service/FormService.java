package com.dynamic_form.service.service;

import com.dynamic_form.service.dto.FormDetailsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormBuilderService formBuilderService;

    public FormDetailsDTO generateForm(String formInput) {
        return this.formBuilderService.extractFormDetails(formInput);
    }
}
