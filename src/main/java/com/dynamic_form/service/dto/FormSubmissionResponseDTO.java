package com.dynamic_form.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionResponseDTO {
    @JsonProperty("submission_id")
    private Long submissionId;
    
    @JsonProperty("form_id")
    private Long formId;
    
    @JsonProperty("form_name")
    private String formName;
    
    @JsonProperty("form_data")
    private Map<String, String> formData;
    
    @JsonProperty("submitted_at")
    private LocalDateTime submittedAt;
}