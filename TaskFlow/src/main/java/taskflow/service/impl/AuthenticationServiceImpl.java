package taskflow.service.impl;

import taskflow.entities.User;
import taskflow.entities.enums.Role;
import taskflow.dto.request.AuthenticationRequest;
import taskflow.dto.request.RegisterRequest;
import taskflow.dto.response.AuthenticationResponse;
import taskflow.repository.UserRepository;
import taskflow.security.JwtService;
import taskflow.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .tokens(user.getToken())
                .build();
    }


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();

        // Fetch more details from the user object
        Long userId = user.getId();
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String email = user.getEmail();
        int tokens = user.getToken();
        Role role = user.getRole();

        // Generate JWT token
        var jwtToken = jwtService.generateToken(user);

        // Create AuthenticationResponse with additional user information
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .tokens(tokens)
                .role(role)
                .build();
    }

}
