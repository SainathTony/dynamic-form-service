package com.dynamic_form.service.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class FormDetailsResponseDTO extends FormDetailsDTO{
    private LocalDateTime created_at;
    private Long id;
}
