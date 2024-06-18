package com.example.ems_app.service.impl;

import com.example.ems_app.dto.LoginRequestDTO;
import com.example.ems_app.dto.LoginResponseDTO;
import com.example.ems_app.dto.UserDTO;
import com.example.ems_app.exception.BadRequestException;
import com.example.ems_app.exception.UnauthorizedException;
import com.example.ems_app.repository.UserRepository;
import com.example.ems_app.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.ems_app.dto.UserDTO.convertToDto;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserDetailsService userDetailsService;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDTO login(LoginRequestDTO request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());

            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(userDetails))
                    .build();
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Authentication failed. Please check your credentials.", e);
        }
    }

    @Override
    public UserDTO register(UserDTO userDTO) {
        if(userRepository.existsByUsername(userDTO.getUsername())) {
            throw new BadRequestException("Username is already in use.");
        }
        String password = userDTO.getPassword();
        userDTO.setPassword(passwordEncoder.encode(password));
        return convertToDto(userRepository.save(userDTO.convertToModel()));
    }
}
