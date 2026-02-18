package com.urbanfix.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    @JsonProperty("user_id")
    private Long userId;
    private String name;
    private String email;
    private String phone;
    @JsonProperty("expires_in")
    private Long expiresIn;
}
