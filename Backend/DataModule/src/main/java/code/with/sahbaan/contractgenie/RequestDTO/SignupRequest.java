package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String fullName;
    private String email;
    private String password;
}
