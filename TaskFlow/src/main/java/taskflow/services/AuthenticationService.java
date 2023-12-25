package taskflow.services;

import org.springframework.stereotype.Component;
import taskflow.dto.requests.AuthenticationRequest;
import taskflow.dto.requests.RegisterRequest;
import taskflow.dto.responses.AuthenticationResponse;

@Component
public interface AuthenticationService {

        AuthenticationResponse register(RegisterRequest user);

        AuthenticationResponse authenticate(AuthenticationRequest user);
}
