package com.urbanfix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintStatusResponse {
    private String status;
    private String note;
    private Instant timestamp;
}
