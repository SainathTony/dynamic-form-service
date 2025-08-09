package com.dynamic_form.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "form_fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "form")
@ToString(exclude = "form")
public class FormField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "field_name", nullable = false, length = 255)
    private String fieldName;
    
    @Column(name = "field_type", nullable = false, length = 100)
    private String fieldType;
    
    @Column(name = "field_order")
    private Integer fieldOrder;
    
    @Column(name = "is_required")
    private Boolean isRequired = false;
    
    @Column(name = "placeholder", length = 500)
    private String placeholder;
    
    @Column(name = "validation_rules", columnDefinition = "TEXT")
    private String validationRules;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;
    
    public FormField(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }
}