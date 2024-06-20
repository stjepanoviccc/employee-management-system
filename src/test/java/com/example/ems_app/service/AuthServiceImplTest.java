package com.example.ems_app.service;

import com.example.ems_app.dto.LoginRequestDTO;
import com.example.ems_app.dto.LoginResponseDTO;
import com.example.ems_app.dto.UserDTO;
import com.example.ems_app.exception.BadRequestException;
import com.example.ems_app.exception.UnauthorizedException;
import com.example.ems_app.model.User;
import com.example.ems_app.repository.UserRepository;
import com.example.ems_app.service.impl.AuthServiceImpl;
import com.example.ems_app.service.impl.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.ems_app.dto.UserDTO.convertToDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    private final User user = createUser();
    LoginRequestDTO request = new LoginRequestDTO(user.getUsername(), user.getPassword());

    private User createUser() {
        return User.builder()
                .id(1L)
                .username("username")
                .password("password")
                .roles("ROLE_ADMIN")
                .build();
    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtServiceImpl jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void shouldLogin_whenLogin_ifCredentialsAreValid() {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());
        UserDetails userDetails = mock(UserDetails.class);
        String mockToken = "mock.token.string";

        when(authenticationManager.authenticate(authenticationToken)).thenReturn(null);
        when(userDetailsService.loadUserByUsername(request.getUsername())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(mockToken);

        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals(mockToken, response.getAccessToken());

        verify(authenticationManager).authenticate(authenticationToken);
        verify(userDetailsService).loadUserByUsername(request.getUsername());
        verify(jwtService).generateToken(userDetails);
    }

    @Test
    void shouldThrowBadRequestException_whenLogin_ifCredentialsAreInvalid() {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        when(authenticationManager.authenticate(authenticationToken))
                .thenThrow(new BadCredentialsException("Invalid credentials"));
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> authService.login(request));

        assertEquals("Authentication failed. Please check your credentials.", exception.getMessage());

        verify(authenticationManager).authenticate(authenticationToken);
        verifyNoMoreInteractions(userDetailsService, jwtService);
    }

    @Test
    void shouldRegisterUser_whenRegister_ifUsernameIsNotTaken() {
        String encodedPassword = "encoded_password";
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodedPassword);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO registeredUser = authService.register(convertToDto(user));

        assertNotNull(registeredUser);
        assertEquals(user.getId(), registeredUser.getId());
        assertEquals(user.getUsername(), registeredUser.getUsername());

        verify(userRepository).existsByUsername(user.getUsername());
        verify(passwordEncoder).encode(user.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowBadRequestException_whenRegister_ifUsernameIsTaken() {
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> authService.register(convertToDto(user)));

        assertEquals("Username is already in use.", exception.getMessage());

        verify(userRepository).existsByUsername(user.getUsername());
        verifyNoMoreInteractions(passwordEncoder, userRepository);
    }
}
