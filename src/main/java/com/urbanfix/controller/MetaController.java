package com.urbanfix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/meta") // Matches Flutter's call to /api/meta/categories
public class MetaController {

    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, Object>>> getCategories() {
        List<Map<String, Object>> categories = new ArrayList<>();
        categories.add(Map.of("id", 1, "name", "Pothole / Road Damage"));
        categories.add(Map.of("id", 2, "name", "Streetlight / Electricity"));
        categories.add(Map.of("id", 3, "name", "Garbage / Debris"));
        categories.add(Map.of("id", 4, "name", "Water Leakage / Drainage"));
        categories.add(Map.of("id", 5, "name", "Fallen Tree / Park Issue"));
        categories.add(Map.of("id", 6, "name", "Traffic Violation"));
        categories.add(Map.of("id", 9, "name", "Other"));
        return ResponseEntity.ok(categories);
    }
}