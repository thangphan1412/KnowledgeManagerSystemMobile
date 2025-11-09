package com.abc.knowledgemanagersystems.dto.request;

import com.abc.knowledgemanagersystems.status.RoleName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    private String email;
    private String password;
    private String username;
    private String address;
    private String numberphone;
    private RoleName roleName;
}
