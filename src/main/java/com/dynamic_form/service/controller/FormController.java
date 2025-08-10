package com.dynamic_form.service.controller;

import com.dynamic_form.service.dto.FormDetailsResponseDTO;
import com.dynamic_form.service.dto.FormSubmissionRequestDTO;
import com.dynamic_form.service.dto.FormSubmissionResponseDTO;
import com.dynamic_form.service.dto.GenerateFormDTO;
import com.dynamic_form.service.dto.ResponseDTO;
import com.dynamic_form.service.exception.ApiException;
import com.dynamic_form.service.exception.FormBuilderException;
import com.dynamic_form.service.exception.ResponseParsingException;
import com.dynamic_form.service.service.FormService;
import com.dynamic_form.service.service.FormSubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {
    private static final Logger logger = LoggerFactory.getLogger(FormController.class);
    private final FormService formService;
    private final FormSubmissionService formSubmissionService;

    @PostMapping("/generate")
    public ResponseEntity<ResponseDTO<FormDetailsResponseDTO>> generateForm(@Valid @RequestBody GenerateFormDTO generateFormDTO) {
        try {
            FormDetailsResponseDTO formDetails = this.formService.generateForm(generateFormDTO.getFormInput());
            ResponseDTO<FormDetailsResponseDTO> response = ResponseDTO.success(formDetails, "Form generated successfully");
            return ResponseEntity.ok(response);
        } catch (ApiException e) {
            logger.error("API error during form generation: {}", e.getMessage(), e);
            ResponseDTO<FormDetailsResponseDTO> errorResponse = ResponseDTO.error("AI service error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
        } catch (ResponseParsingException e) {
            logger.error("Response parsing error during form generation: {}", e.getMessage(), e);
            ResponseDTO<FormDetailsResponseDTO> errorResponse = ResponseDTO.error("Invalid response from AI service: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (FormBuilderException e) {
            logger.error("Form builder error during form generation: {}", e.getMessage(), e);
            ResponseDTO<FormDetailsResponseDTO> errorResponse = ResponseDTO.error("Form generation error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error during form generation", e);
            ResponseDTO<FormDetailsResponseDTO> errorResponse = ResponseDTO.error("An unexpected error occurred. Please try again later.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping
    public ResponseEntity<ResponseDTO<List<FormDetailsResponseDTO>>> getAllForms() {
        try {
            List<FormDetailsResponseDTO> forms = this.formService.getAllForms();
            ResponseDTO<List<FormDetailsResponseDTO>> response = ResponseDTO.success(forms, "Forms retrieved successfully");
            logger.info("Retrieved {} forms from database", forms.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving all forms", e);
            ResponseDTO<List<FormDetailsResponseDTO>> errorResponse = ResponseDTO.error("An unexpected error occurred while retrieving forms.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/submit")
    public ResponseEntity<ResponseDTO<FormSubmissionResponseDTO>> submitForm(
            @Valid @RequestBody FormSubmissionRequestDTO requestDTO) {
        try {
            FormSubmissionResponseDTO submission = formSubmissionService.submitForm(requestDTO);
            ResponseDTO<FormSubmissionResponseDTO> response = ResponseDTO.success(submission, "Form submitted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error submitting form: {}", e.getMessage(), e);
            ResponseDTO<FormSubmissionResponseDTO> errorResponse = ResponseDTO.error(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error during form submission", e);
            ResponseDTO<FormSubmissionResponseDTO> errorResponse = ResponseDTO.error("An unexpected error occurred during form submission.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/{formId}/submissions")
    public ResponseEntity<ResponseDTO<List<FormSubmissionResponseDTO>>> getFormSubmissions(
            @PathVariable Long formId,
            @RequestParam(required = false) String searchTerm) {
        try {
            List<FormSubmissionResponseDTO> submissions = formSubmissionService.getFormSubmissions(formId, searchTerm);
            ResponseDTO<List<FormSubmissionResponseDTO>> response = ResponseDTO.success(submissions, "Form submissions retrieved successfully");
            logger.info("Retrieved {} submissions for form ID: {}", submissions.size(), formId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            logger.error("Error retrieving form submissions: {}", e.getMessage(), e);
            ResponseDTO<List<FormSubmissionResponseDTO>> errorResponse = ResponseDTO.error(e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error while retrieving form submissions for form ID: {}", formId, e);
            ResponseDTO<List<FormSubmissionResponseDTO>> errorResponse = ResponseDTO.error("An unexpected error occurred while retrieving form submissions.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
