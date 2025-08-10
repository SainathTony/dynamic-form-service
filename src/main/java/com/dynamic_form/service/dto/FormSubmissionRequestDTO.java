package com.dynamic_form.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormSubmissionRequestDTO {
    @JsonProperty("form_id")
    private Long formId;
    
    @JsonProperty("form_data")
    private Map<Long, String> formData;
}