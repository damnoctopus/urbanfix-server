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
public class ComplaintSubmissionResponse {
    private Long id;
    private String status;
    @JsonProperty("created_at")
    private Instant createdAt;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("priority_score")
    private Double priorityScore;
}
