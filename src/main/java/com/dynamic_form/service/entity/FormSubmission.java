package com.dynamic_form.service.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_submissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"form", "submissionFields"})
@ToString(exclude = {"form", "submissionFields"})
public class FormSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;
    
    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;
    
    @OneToMany(mappedBy = "formSubmission", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormSubmissionField> submissionFields = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
    }
    
    public void addSubmissionField(FormSubmissionField submissionField) {
        submissionFields.add(submissionField);
        submissionField.setFormSubmission(this);
    }
}