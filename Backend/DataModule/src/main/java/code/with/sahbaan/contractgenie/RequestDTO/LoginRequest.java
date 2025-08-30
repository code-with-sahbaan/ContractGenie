package code.with.sahbaan.contractgenie.RequestDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotNull(message = "Email is Required")
    @Size(min = 3, max = 100, message = "Email must be within 3 - 100 chars limit")
    private String email;

    @NotNull(message = "Password is Required")
    @Size(min = 8, message = "Email must be of 8 chars")
    private String password;
}
