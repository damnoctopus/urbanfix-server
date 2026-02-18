package com.urbanfix.service;

import jakarta.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeminiService {
    @Value("${gemini.model:gemini-1.0-pro}")
    private String model;

    @Value("${gemini.api-url:https://generativelanguage.googleapis.com/v1beta/models}")
    private String apiUrl;

    @Value("${gemini.api-key:}")
    private String apiKey;

    private final Map<String, List<String>> departmentKeywords = new LinkedHashMap<>();

    @PostConstruct
    public void init() {
        departmentKeywords.put("BBMP / PWD", List.of("pothole", "road", "asphalt", "pavement", "traffic", "flyover"));
        departmentKeywords.put("BWSSB", List.of("water", "leak", "pipeline", "hydrant", "supply", "drainage", "sewage"));
        departmentKeywords.put("BESCOM", List.of("power", "electricity", "light", "streetlight", "outage", "wire", "pole"));
        departmentKeywords.put("BBMP (SWM)", List.of("garbage", "waste", "trash", "sanitation", "dustbin", "debris"));
        departmentKeywords.put("BBMP (Forestry)", List.of("park", "playground", "trees", "garden", "branch", "leaf"));
        departmentKeywords.put("BBMP (General)", List.of("general", "other", "misc", "nuisance"));
    }

    public String routeComplaint(String description, String category) {
        String sanitized = (category + " " + description).toLowerCase(Locale.ENGLISH);
        for (Map.Entry<String, List<String>> entry : departmentKeywords.entrySet()) {
            for (String keyword : entry.getValue()) {
                if (sanitized.contains(keyword)) {
                    log.info("Complaint routed to {} based on keyword '{}'", entry.getKey(), keyword);
                    return entry.getKey();
                }
            }
        }
        return "BBMP (General)";
    }
}