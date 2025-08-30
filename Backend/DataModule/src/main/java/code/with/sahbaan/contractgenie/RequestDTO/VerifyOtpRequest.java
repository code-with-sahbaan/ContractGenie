package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequest {

    private String email;
    private String otp;
    private String verificationType;
    // for Forgot Password
    private String password;
}
