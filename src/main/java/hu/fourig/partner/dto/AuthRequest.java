package hu.fourig.partner.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthRequest {
    private String username;
    private String password;
}