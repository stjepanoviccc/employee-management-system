package com.example.ems_app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

    @Schema(description = "Username of the user", example = "admin")
    private String username;

    @Schema(description = "Password of the user", example = "password")
    private String password;
}
