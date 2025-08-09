package com.dynamic_form.service.repository;

import com.dynamic_form.service.entity.FormField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    
    List<FormField> findByFormIdOrderByFieldOrder(Long formId);
    
    List<FormField> findByFieldType(String fieldType);
    
    List<FormField> findByIsRequired(Boolean isRequired);
    
    @Query("SELECT f FROM FormField f WHERE f.form.id = :formId AND f.fieldName = :fieldName")
    FormField findByFormIdAndFieldName(@Param("formId") Long formId, @Param("fieldName") String fieldName);
    
    @Query("SELECT COUNT(f) FROM FormField f WHERE f.form.id = :formId")
    long countByFormId(@Param("formId") Long formId);
}