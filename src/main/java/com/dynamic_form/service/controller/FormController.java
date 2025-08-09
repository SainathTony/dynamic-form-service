package com.dynamic_form.service.controller;

import com.dynamic_form.service.dto.FormDetailsDTO;
import com.dynamic_form.service.dto.FormGenerationResponseDTO;
import com.dynamic_form.service.dto.GenerateFormDTO;
import com.dynamic_form.service.exception.ApiException;
import com.dynamic_form.service.exception.FormBuilderException;
import com.dynamic_form.service.exception.ResponseParsingException;
import com.dynamic_form.service.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/forms")
@RequiredArgsConstructor
public class FormController {
    private static final Logger logger = LoggerFactory.getLogger(FormController.class);
    private final FormService formService;

    @PostMapping("/generate")
    public ResponseEntity<FormGenerationResponseDTO> generateForm(@Valid @RequestBody GenerateFormDTO generateFormDTO) {
        try {
            FormDetailsDTO formDetails = this.formService.generateForm(generateFormDTO.getFormInput());
            FormGenerationResponseDTO response = FormGenerationResponseDTO.success(formDetails);
            return ResponseEntity.ok(response);
        } catch (ApiException e) {
            logger.error("API error during form generation: {}", e.getMessage(), e);
            FormGenerationResponseDTO errorResponse = FormGenerationResponseDTO.error("AI service error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_GATEWAY);
        } catch (ResponseParsingException e) {
            logger.error("Response parsing error during form generation: {}", e.getMessage(), e);
            FormGenerationResponseDTO errorResponse = FormGenerationResponseDTO.error("Invalid response from AI service: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (FormBuilderException e) {
            logger.error("Form builder error during form generation: {}", e.getMessage(), e);
            FormGenerationResponseDTO errorResponse = FormGenerationResponseDTO.error("Form generation error: " + e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error during form generation", e);
            FormGenerationResponseDTO errorResponse = FormGenerationResponseDTO.error("An unexpected error occurred. Please try again later.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
