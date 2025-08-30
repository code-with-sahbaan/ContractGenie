package code.with.sahbaan.contractgenie.Services;


import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.RequestDTO.ForgotPassword;
import code.with.sahbaan.contractgenie.RequestDTO.SignupRequest;
import code.with.sahbaan.contractgenie.RequestDTO.VerifyOtpRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;

import java.util.Optional;

public interface UserService {

    Optional<Users> getUserByEmail(String email);

    BaseResponse<?> signup(SignupRequest signupRequest) throws Exception;

    void verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception;

    Users getCurrentUser();

    void forgotPassword(ForgotPassword forgotPassword) throws Exception;

    void updateUser(Users users) throws Exception;

}
