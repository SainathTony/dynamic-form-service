package com.dynamic_form.service.service;

import com.dynamic_form.service.dto.FormDetailsDTO;
import com.dynamic_form.service.dto.FormFieldDTO;
import com.dynamic_form.service.entity.Form;
import com.dynamic_form.service.entity.FormField;
import com.dynamic_form.service.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormService {
    private static final Logger logger = LoggerFactory.getLogger(FormService.class);
    
    private final FormBuilderService formBuilderService;
    private final FormRepository formRepository;

    @Transactional
    public FormDetailsDTO generateForm(String formInput) {
        logger.info("Generating form from input with length: {}", formInput.length());
        
        FormDetailsDTO formDetails = this.formBuilderService.extractFormDetails(formInput);
        
        Form savedForm = saveFormToDatabase(formDetails, formInput);
        
        logger.info("Form saved to database with ID: {}", savedForm.getId());
        
        return formDetails;
    }
    
    private Form saveFormToDatabase(FormDetailsDTO formDetails, String originalInput) {
        Form form = new Form();
        form.setFormName(formDetails.getFormName());
        form.setOriginalInput(originalInput);
        
        for (int i = 0; i < formDetails.getFields().size(); i++) {
            FormFieldDTO fieldDTO = formDetails.getFields().get(i);
            FormField field = new FormField(fieldDTO.getFieldName(), fieldDTO.getFieldType());
            field.setFieldOrder(i + 1);
            field.setIsRequired(false);
            form.addField(field);
        }
        
        return formRepository.save(form);
    }
    
    private FormDetailsDTO convertToDTO(Form form) {
        FormDetailsDTO dto = new FormDetailsDTO();
        dto.setFormName(form.getFormName());
        
        dto.setFields(form.getFields().stream()
                .sorted((f1, f2) -> Integer.compare(f1.getFieldOrder() != null ? f1.getFieldOrder() : 0, 
                                                   f2.getFieldOrder() != null ? f2.getFieldOrder() : 0))
                .map(field -> new FormFieldDTO(field.getFieldName(), field.getFieldType()))
                .toList());
        
        return dto;
    }
}
