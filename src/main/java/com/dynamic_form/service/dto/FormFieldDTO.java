package com.dynamic_form.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldDTO {
    @JsonProperty("field_name")
    private String fieldName;
    
    @JsonProperty("field_type")
    private String fieldType;
}