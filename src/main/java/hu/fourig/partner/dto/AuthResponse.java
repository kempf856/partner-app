package hu.fourig.partner.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AuthResponse {
    private String token;
}