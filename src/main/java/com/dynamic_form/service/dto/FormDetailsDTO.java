package com.dynamic_form.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDetailsDTO {
    @JsonProperty("form_name")
    private String formName;
    
    @JsonProperty("fields")
    private List<FormFieldDTO> fields;
}