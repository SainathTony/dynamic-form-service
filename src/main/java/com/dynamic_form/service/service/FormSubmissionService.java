package com.dynamic_form.service.service;

import com.dynamic_form.service.dto.FormSubmissionRequestDTO;
import com.dynamic_form.service.dto.FormSubmissionResponseDTO;
import com.dynamic_form.service.entity.Form;
import com.dynamic_form.service.entity.FormField;
import com.dynamic_form.service.entity.FormSubmission;
import com.dynamic_form.service.entity.FormSubmissionField;
import com.dynamic_form.service.repository.FormRepository;
import com.dynamic_form.service.repository.FormSubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FormSubmissionService {
    private static final Logger logger = LoggerFactory.getLogger(FormSubmissionService.class);
    
    private final FormRepository formRepository;
    private final FormSubmissionRepository formSubmissionRepository;

    @Transactional
    public FormSubmissionResponseDTO submitForm(FormSubmissionRequestDTO request) {
        logger.info("Processing form submission for form ID: {}", request.getFormId());
        
        // Find the form
        Form form = formRepository.findById(request.getFormId())
                .orElseThrow(() -> new RuntimeException("Form not found with ID: " + request.getFormId()));
        
        // Validate required fields
        validateRequiredFields(form, request.getFormData());
        
        // Create form submission
        FormSubmission submission = new FormSubmission();
        submission.setForm(form);
        
        // Create submission fields
        Map<Long, FormField> fieldMap = createFieldMap(form.getFields());
        
        for (Map.Entry<Long, String> entry : request.getFormData().entrySet()) {
            Long fieldId = entry.getKey();
            String fieldValue = entry.getValue();
            
            FormField formField = fieldMap.get(fieldId);
            if (formField != null) {
                FormSubmissionField submissionField = new FormSubmissionField(formField, fieldValue);
                submission.addSubmissionField(submissionField);
            } else {
                logger.warn("Unknown field submitted for form ID: {}", request.getFormId());
            }
        }
        
        // Save submission
        FormSubmission savedSubmission = formSubmissionRepository.save(submission);
        
        logger.info("Form submission saved with ID: {}", savedSubmission.getId());
        
        return convertToResponseDTO(savedSubmission);
    }
    
    public List<FormSubmissionResponseDTO> getFormSubmissions(Long formId) {
        logger.info("Fetching submissions for form ID: {}", formId);
        
        if (!formRepository.existsById(formId)) {
            throw new RuntimeException("Form not found with ID: " + formId);
        }
        
        return formSubmissionRepository.findByFormIdOrderBySubmittedAtDesc(formId).stream()
                .map(this::convertToResponseDTO)
                .toList();
    }
    
    private void validateRequiredFields(Form form, Map<Long, String> formData) {
        for (FormField field : form.getFields()) {
            if (Boolean.TRUE.equals(field.getIsRequired())) {
                String value = formData.get(field.getId());
                if (value == null || value.trim().isEmpty()) {
                    throw new RuntimeException("Required field '" + field.getFieldName() + "' is missing or empty");
                }
            }
        }
    }
    
    private Map<Long, FormField> createFieldMap(List<FormField> fields) {
        Map<Long, FormField> fieldMap = new HashMap<>();
        for (FormField field : fields) {
            fieldMap.put(field.getId(), field);
        }
        return fieldMap;
    }
    
    private FormSubmissionResponseDTO convertToResponseDTO(FormSubmission submission) {
        FormSubmissionResponseDTO dto = new FormSubmissionResponseDTO();
        dto.setSubmissionId(submission.getId());
        dto.setFormId(submission.getForm().getId());
        dto.setFormName(submission.getForm().getFormName());
        dto.setSubmittedAt(submission.getSubmittedAt());
        
        // Convert submission fields to map
        Map<String, String> formData = new HashMap<>();
        for (FormSubmissionField field : submission.getSubmissionFields()) {
            formData.put(field.getFormField().getFieldName(), field.getFieldValue());
        }
        dto.setFormData(formData);
        
        return dto;
    }
}