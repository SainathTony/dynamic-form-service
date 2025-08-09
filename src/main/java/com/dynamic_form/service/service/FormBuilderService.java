package com.dynamic_form.service.service;

import com.dynamic_form.service.dto.FormDetailsDTO;
import com.dynamic_form.service.dto.FormFieldDTO;
import com.dynamic_form.service.exception.ApiException;
import com.dynamic_form.service.exception.FormBuilderException;
import com.dynamic_form.service.exception.ResponseParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FormBuilderService {
    private static final Logger logger = LoggerFactory.getLogger(FormBuilderService.class);
    
    @Value("${groq.api.key}")
    private String GROQ_API_KEY;

    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";
    private static final String MODEL_NAME = "llama-3.1-8b-instant";
    private final ObjectMapper mapper = new ObjectMapper();

    public FormDetailsDTO extractFormDetails(String userInput) {
        OkHttpClient client = new OkHttpClient();
        logger.info("Starting form details extraction for input length: {}", userInput.length());

        String prompt = String.format("""
                Extract the form name and form fields from the following text.
                Respond ONLY in JSON format without any additional text:
                {
                  "form_name": "<Form Name>",
                  "fields": [{field_name: "Field1", field_type: <Type>}, {field_name: "Field2", field_type: <Type>}, ...]
                }
                Text: "%s"
                """, userInput);

        try {
            String jsonRequest = mapper.writeValueAsString(Map.of(
                    "model", MODEL_NAME,
                    "messages", new Object[]{
                            Map.of("role", "system", "content", "You are a form extraction assistant. Always respond with valid JSON only."),
                            Map.of("role", "user", "content", prompt)
                    },
                    "temperature", 0,
                    "max_tokens", 1000
            ));

            Request request = new Request.Builder()
                    .url(GROQ_API_URL)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + GROQ_API_KEY)
                    .post(RequestBody.create(jsonRequest, MediaType.parse("application/json")))
                    .build();

            return executeRequest(client, request);
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize request JSON", e);
            throw new FormBuilderException("Failed to prepare API request", e);
        }
    }

    private FormDetailsDTO executeRequest(OkHttpClient client, Request request) {
        try (Response response = client.newCall(request).execute()) {
            return handleApiResponse(response);
        } catch (IOException e) {
            logger.error("Network error during API call", e);
            throw new FormBuilderException("Failed to communicate with AI service", e);
        }
    }

    private FormDetailsDTO handleApiResponse(Response response) throws IOException {
        if (!response.isSuccessful()) {
            String errorBody = response.body().string();
            logger.error("API request failed with status: {}, body: {}", response.code(), errorBody);
            throw new ApiException("AI service request failed: " + response.message(), response.code());
        }

        ResponseBody responseBody = response.body();

        String responseBodyString = responseBody.string();
        logger.debug("Received API response: {}", responseBodyString);

        return parseApiResponse(responseBodyString);
    }

    private FormDetailsDTO parseApiResponse(String responseBodyString) {
        try {
            JsonNode root = mapper.readTree(responseBodyString);
            
            if (!root.has("choices") || !root.get("choices").isArray() || root.get("choices").isEmpty()) {
                throw new ResponseParsingException("Invalid API response structure: missing or empty choices array");
            }

            JsonNode choice = root.get("choices").get(0);
            if (!choice.has("message") || !choice.get("message").has("content")) {
                throw new ResponseParsingException("Invalid API response structure: missing message content");
            }

            String content = choice.get("message").get("content").asText();
            if (!StringUtils.hasText(content)) {
                throw new ResponseParsingException("Empty content received from AI service");
            }

            return parseFormDetails(content.trim());
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse API response JSON", e);
            throw new ResponseParsingException("Invalid JSON response from AI service", e);
        }
    }

    private FormDetailsDTO parseFormDetails(String content) {
        try {
            content = cleanJsonContent(content);
            
            FormDetailsDTO formDetails = mapper.readValue(content, FormDetailsDTO.class);
            
            validateFormDetails(formDetails);
            
            logger.info("Successfully extracted form details: form_name='{}', fields_count={}", 
                    formDetails.getFormName(), 
                    formDetails.getFields() != null ? formDetails.getFields().size() : 0);
            
            return formDetails;
            
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse form details JSON: {}", content, e);
            throw new ResponseParsingException("AI service returned invalid JSON format", e);
        }
    }

    private String cleanJsonContent(String content) {
        content = content.trim();
        
        if (content.startsWith("```json")) {
            content = content.substring(7);
        }
        if (content.startsWith("```")) {
            content = content.substring(3);
        }
        if (content.endsWith("```")) {
            content = content.substring(0, content.length() - 3);
        }
        
        return content.trim();
    }

    private void validateFormDetails(FormDetailsDTO formDetails) {
        if (formDetails == null) {
            throw new ResponseParsingException("Form details object is null");
        }
        
        if (!StringUtils.hasText(formDetails.getFormName())) {
            throw new ResponseParsingException("Invalid or empty form_name in AI response");
        }
        
        if (formDetails.getFields() == null || formDetails.getFields().isEmpty()) {
            throw new ResponseParsingException("At least one field is required in AI response");
        }
        
        for (int i = 0; i < formDetails.getFields().size(); i++) {
            FormFieldDTO field = formDetails.getFields().get(i);
            if (field == null) {
                throw new ResponseParsingException("Field at index " + i + " is null");
            }
            if (!StringUtils.hasText(field.getFieldName())) {
                throw new ResponseParsingException("Field at index " + i + " has invalid or empty field_name");
            }
            if (!StringUtils.hasText(field.getFieldType())) {
                throw new ResponseParsingException("Field at index " + i + " has invalid or empty field_type");
            }
        }
    }
}
