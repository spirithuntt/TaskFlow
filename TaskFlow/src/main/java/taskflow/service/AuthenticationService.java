package taskflow.service;

import org.springframework.stereotype.Component;
import taskflow.dto.request.AuthenticationRequest;
import taskflow.dto.request.RegisterRequest;
import taskflow.dto.response.AuthenticationResponse;

@Component
public interface AuthenticationService {

        AuthenticationResponse register(RegisterRequest user);

        AuthenticationResponse authenticate(AuthenticationRequest user);
}
