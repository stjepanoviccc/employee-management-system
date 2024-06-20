package com.example.ems_app.dto;

import com.example.ems_app.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @Schema(description = "Username of the user", example = "admin")
    private String username;

    @Schema(description = "Password of the user", example = "password")
    private String password;

    @Schema(description = "Roles assigned to the user", example = "ROLE_ADMIN")
    private String roles;

    public static UserDTO convertToDto(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles())
                .build();
    }

    public User convertToModel() {
        return User.builder()
                .id(getId())
                .username(getUsername())
                .password(getPassword())
                .roles(getRoles())
                .build();
    }
}
