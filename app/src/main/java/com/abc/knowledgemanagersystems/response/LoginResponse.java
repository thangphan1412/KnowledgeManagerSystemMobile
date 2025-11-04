package com.abc.knowledgemanagersystems.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String jwtToken;
    private String role; // Ví dụ: "ADMIN", "RESEARCHER", "TECHNICIAN"
    private int userId;
}
