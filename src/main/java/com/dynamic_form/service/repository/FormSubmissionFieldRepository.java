package com.dynamic_form.service.repository;

import com.dynamic_form.service.entity.FormSubmissionField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormSubmissionFieldRepository extends JpaRepository<FormSubmissionField, Long> { }