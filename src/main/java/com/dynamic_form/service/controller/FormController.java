package com.dynamic_form.service.controller;

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
    public ResponseEntity<Map<String, Object>> generateForm(@Valid @RequestBody GenerateFormDTO generateFormDTO) {
        try {
            Map<String, Object> response = this.formService.generateForm(generateFormDTO.getFormInput());
            return ResponseEntity.ok(response);
        } catch (ApiException e) {
            logger.error("API error during form generation: {}", e.getMessage(), e);
            return buildErrorResponse("AI service error: " + e.getMessage(), HttpStatus.BAD_GATEWAY);
        } catch (ResponseParsingException e) {
            logger.error("Response parsing error during form generation: {}", e.getMessage(), e);
            return buildErrorResponse("Invalid response from AI service: " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (FormBuilderException e) {
            logger.error("Form builder error during form generation: {}", e.getMessage(), e);
            return buildErrorResponse("Form generation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.error("Unexpected error during form generation", e);
            return buildErrorResponse("An unexpected error occurred. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", true);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("status", status.value());
        return new ResponseEntity<>(errorResponse, status);
    }
}
