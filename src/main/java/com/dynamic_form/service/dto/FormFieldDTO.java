package com.dynamic_form.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldDTO {
    @JsonProperty("name")
    private String fieldName;
    
    @JsonProperty("type")
    private String fieldType;
    
    @JsonProperty("placeholder")
    private String placeholder;
    
    @JsonProperty("required")
    private Boolean required;
}