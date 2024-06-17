package com.example.ems_app.dto;

import com.example.ems_app.model.User;
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
    private String username;
    private String password;
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
