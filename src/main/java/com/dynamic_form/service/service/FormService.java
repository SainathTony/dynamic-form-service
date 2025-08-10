package com.dynamic_form.service.service;

import com.dynamic_form.service.dto.FormDetailsDTO;
import com.dynamic_form.service.dto.FormDetailsResponseDTO;
import com.dynamic_form.service.dto.FormFieldDTO;
import com.dynamic_form.service.entity.Form;
import com.dynamic_form.service.entity.FormField;
import com.dynamic_form.service.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

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
            field.setIsRequired(fieldDTO.getRequired() != null ? fieldDTO.getRequired() : false);
            field.setPlaceholder(fieldDTO.getPlaceholder());
            form.addField(field);
        }
        
        return formRepository.save(form);
    }
    
    public List<FormDetailsResponseDTO> getAllForms() {
        logger.info("Fetching all forms from database");
        return formRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    private FormDetailsResponseDTO convertToDTO(Form form) {
        FormDetailsResponseDTO dto = new FormDetailsResponseDTO();
        dto.setFormName(form.getFormName());
        
        dto.setFields(form.getFields().stream()
                .sorted(Comparator.comparingInt(f -> f.getFieldOrder() != null ? f.getFieldOrder() : 0))
                .map(field -> new FormFieldDTO(field.getFieldName(), field.getFieldType(), null, field.getIsRequired()))
                .toList());
        dto.setCreated_at(form.getCreatedAt());
        
        return dto;
    }
}
