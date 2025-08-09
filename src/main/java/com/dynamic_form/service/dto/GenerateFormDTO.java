package com.dynamic_form.service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GenerateFormDTO {
    @NotBlank(message = "Form generation guidelines are required")
    @Size(min = 10, max = 2000, message = "Input must be between 10 and 2000 characters")
    private String formInput;
}
