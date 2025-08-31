package code.with.sahbaan.contractgenie.Controllers.v1;

import code.with.sahbaan.contractgenie.RequestDTO.ForgotPassword;
import code.with.sahbaan.contractgenie.RequestDTO.SignupRequest;
import code.with.sahbaan.contractgenie.RequestDTO.VerifyOtpRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.Services.FolderService;
import code.with.sahbaan.contractgenie.Services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 *A Controller that is accessible to every type of role
 * */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private FolderService folderService;

    @PostMapping("v1/signup")
    public ResponseEntity<BaseResponse<?>> signup(@RequestBody SignupRequest signupRequest) throws Exception {
        log.info("Executing signup in UserController");
        BaseResponse<?> response = userService.signup(signupRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("v1/verifyOtp")
    public ResponseEntity<BaseResponse<?>> verifyOtp(@RequestBody VerifyOtpRequest verifyOtpRequest) throws Exception {
        log.info("Executing verifyOtp in UserController");
        userService.verifyOtp(verifyOtpRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("v1/forgotPassword")
    public ResponseEntity<BaseResponse<?>> forgotPassword(@RequestBody ForgotPassword forgotPassword) throws Exception {
        log.info("Executing forgotPassword in UserController");
        userService.forgotPassword(forgotPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*
    * ABOVE THIS LINE EVERY REQUEST IS NON_TOKENIZED. ALWAYS ADD NON_TOKENIZED REQUEST ABOVE THIS.
    * */



}
