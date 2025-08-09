package com.dynamic_form.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormGenerationResponseDTO {
    private boolean success;
    private String message;
    private FormDetailsDTO formDetails;
    private long timestamp;
    
    public static FormGenerationResponseDTO success(FormDetailsDTO formDetails) {
        return new FormGenerationResponseDTO(
            true, 
            "Form details extracted successfully", 
            formDetails, 
            System.currentTimeMillis()
        );
    }
    
    public static FormGenerationResponseDTO error(String message) {
        return new FormGenerationResponseDTO(
            false, 
            message, 
            null, 
            System.currentTimeMillis()
        );
    }
}