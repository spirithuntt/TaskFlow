package taskflow.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import taskflow.entities.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse{

    private String token;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private int tokens;
}
