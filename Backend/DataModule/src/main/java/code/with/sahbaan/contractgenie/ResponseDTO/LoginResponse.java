package code.with.sahbaan.contractgenie.ResponseDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private long userId;
    private String email;
    private String fullName;
    private String accessToken;
    private String role;
    private Boolean isActive;
}
