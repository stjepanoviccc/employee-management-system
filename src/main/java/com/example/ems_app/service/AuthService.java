package com.example.ems_app.service;

import com.example.ems_app.dto.LoginRequestDTO;
import com.example.ems_app.dto.LoginResponseDTO;
import com.example.ems_app.dto.UserDTO;

public interface AuthService {

    LoginResponseDTO login(LoginRequestDTO request);
    UserDTO register(UserDTO userDTO);
}
