package com.dynamic_form.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "form_submission_fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"formSubmission", "formField"})
@ToString(exclude = {"formSubmission", "formField"})
public class FormSubmissionField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_submission_id", nullable = false)
    private FormSubmission formSubmission;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_field_id", nullable = false)
    private FormField formField;
    
    @Column(name = "field_value", columnDefinition = "TEXT")
    private String fieldValue;
    
    public FormSubmissionField(FormField formField, String fieldValue) {
        this.formField = formField;
        this.fieldValue = fieldValue;
    }
}