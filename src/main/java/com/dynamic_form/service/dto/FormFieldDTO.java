package com.dynamic_form.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormFieldDTO {
    private Long id;

    @JsonProperty("name")
    private String fieldName;
    
    @JsonProperty("type")
    private String fieldType;
    
    @JsonProperty("placeholder")
    private String placeholder;
    
    @JsonProperty("required")
    private Boolean required;
    
    @JsonProperty(value = "options", required = false)
    private List<String> options;
}