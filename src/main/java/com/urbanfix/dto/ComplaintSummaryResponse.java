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
public class ComplaintSummaryResponse {
    private Long id;
    private String status;
    @JsonProperty("priority_score")
    private Double priorityScore;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("created_at")
    private Instant createdAt;
}
