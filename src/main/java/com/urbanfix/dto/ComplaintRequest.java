package com.urbanfix.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComplaintRequest {
    @NotBlank(message = "Description is required")
    private String description;

    // FIX: Removed @NotBlank. Backend auto-detects this.
    private String category;

    private Double priority;

    // FIX: Added Latitude and Longitude to match Flutter App
    private Double latitude;
    private Double longitude;

    private MultipartFile image;

    private Integer severity;


    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public Double getPriority() { return priority; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public MultipartFile getImage() { return image; }
    public Integer getSeverity() { return severity; }
}