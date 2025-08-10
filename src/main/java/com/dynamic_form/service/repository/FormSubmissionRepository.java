package com.dynamic_form.service.repository;

import com.dynamic_form.service.entity.FormSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormSubmissionRepository extends JpaRepository<FormSubmission, Long> {
    
    @Query("SELECT fs FROM FormSubmission fs WHERE fs.form.id = :formId ORDER BY fs.submittedAt DESC")
    List<FormSubmission> findByFormIdOrderBySubmittedAtDesc(@Param("formId") Long formId);
    
    @Query("SELECT DISTINCT fs FROM FormSubmission fs " +
           "LEFT JOIN fs.submissionFields sf " +
           "WHERE fs.form.id = :formId " +
           "AND (:searchTerm IS NULL OR LOWER(sf.fieldValue) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) " +
           "ORDER BY fs.submittedAt DESC")
    List<FormSubmission> findByFormIdWithSearchOrderBySubmittedAtDesc(@Param("formId") Long formId, @Param("searchTerm") String searchTerm);
}