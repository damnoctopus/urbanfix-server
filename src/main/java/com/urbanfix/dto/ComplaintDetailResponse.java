package com.urbanfix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintDetailResponse {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private String category;
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer severity;
    @JsonProperty("priority_score")
    private Double priorityScore;
    @JsonProperty("image_url")
    private String imageUrl;
    private String status;
    @JsonProperty("assigned_department")
    private String assignedDepartment;
    @JsonProperty("created_at")
    private Instant createdAt;
}
