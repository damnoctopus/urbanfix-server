package com.urbanfix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintResponse {
    private Long id;
    private Long userId;
    private String userName;
    private String description;
    private String category;
    private String department;
    private Double priority;
    private String status;
    private String imagePath;
    private Double latitude; // Added
    private Double longitude; // Added
    private LocalDateTime createdAt;
}