package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.AppConfigs;
import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Repositories.UserRepository;
import code.with.sahbaan.contractgenie.RequestDTO.ForgotPassword;
import code.with.sahbaan.contractgenie.RequestDTO.SendEmail;
import code.with.sahbaan.contractgenie.RequestDTO.SignupRequest;
import code.with.sahbaan.contractgenie.RequestDTO.VerifyOtpRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.Services.AppConfigService;
import code.with.sahbaan.contractgenie.Services.UserService;
import code.with.sahbaan.contractgenie.Utils.Constants;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl extends GenericServiceImpl<Users> implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppConfigService appConfigService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${contractgenie.email.sender}")
    private String emailSender;

    @Value("${spring.application.name}")
    private String appName;

    public UserServiceImpl() {
        super(Users.class);
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Loading user by email
        Optional<Users> user = userRepository.findByEmail(email);
        if (user.isEmpty()){
            // Throwing error if user is not found
            throw new UsernameNotFoundException("User with this email does not exist");
        } else{
            // if user found, collecting authorities and returning User object
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            return new org.springframework.security.core.userdetails.User(user.get().getEmail(), user.get().getPassword(), authorities);
        }
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        // finding user by email
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public BaseResponse<?> signup(SignupRequest signupRequest) throws Exception {
        /*
        * Checking if user exists with this email
        * */
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("User Already Exists with this Email");
        }
        try{
            Users users = convertDtoToEntity(signupRequest);
            users.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
            Users savedUser = userRepository.save(users);
            return new BaseResponse<>("Account Created Successfully", null);
        } catch (Exception e) {
            throw new Exception("Failed to Signup");
        }
    }

    @Override
    public void verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception {
        Users users = userRepository.findByEmail(verifyOtpRequest.getEmail()).get();
        if (verifyOtpRequest.getVerificationType().equalsIgnoreCase("profileActivation")){
            // Profile Verification
            if (!users.getEmailOTP().equals(verifyOtpRequest.getOtp())){
                throw new Exception("Verification failed due to incorrect OTP");
            }
            users.setIsActive(true);
        }else{
            // Password Reset
            if (!users.getForgotPasswordOTP().equals(verifyOtpRequest.getOtp())){
                throw new Exception("Verification failed due to incorrect OTP");
            }
            users.setPassword(new BCryptPasswordEncoder().encode(verifyOtpRequest.getPassword()));
        }
        userRepository.save(users);
    }

    @Override
    public Users getCurrentUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return getUserByEmail(email).get();
    }

    @Override
    public void forgotPassword(ForgotPassword forgotPassword) throws Exception {
        Users users;
        try{
            users = getUserByEmail(forgotPassword.getEmail()).get();
        }catch (Exception e){
            throw new Exception("No User exists with this email");
        }
        try{
            sendForgotPasswordOTP(users);
        } catch (Exception e) {
            throw new Exception("failed to Send OTP");
        }
    }


    @Override
    public void updateUser(Users users) throws Exception {
        userRepository.save(users);
    }

    public void sendOTP(Users users) throws Exception {
        // Creating OTP
        String otp = Integer.toString(generateRandomNumber());
        users.setEmailOTP(otp);
        userRepository.save(users);
        // Sending Email
        SendEmail sendEmail = new SendEmail();
        sendEmail.setToEmail(users.getEmail());
        sendEmail.setSubject("User Activation");
        sendEmail.setEmailTemplateName(Constants.EMAIL_OTP_TEMPLATE_NAME);
        Map<String,String> emailContent = new HashMap<>();
        emailContent.put("code",otp);
        emailContent.put("name",users.getFullName());
        sendEmail.setContent(emailContent);
        initiateEmail(sendEmail);
    }

    private void sendForgotPasswordOTP(Users users) throws Exception {
        // Creating OTP
        String otp = Integer.toString(generateRandomNumber());
        users.setForgotPasswordOTP(otp);
        userRepository.save(users);
        // Sending Email
        SendEmail sendEmail = new SendEmail();
        sendEmail.setToEmail(users.getEmail());
        sendEmail.setSubject("Reset Password");
        sendEmail.setEmailTemplateName(Constants.FORGOT_PASSWORD_OTP_TEMPLATE_NAME);
        Map<String,String> emailContent = new HashMap<>();
        emailContent.put("code",otp);
        emailContent.put("name",users.getFullName());
        sendEmail.setContent(emailContent);
        initiateEmail(sendEmail);
    }

    private int generateRandomNumber() {
        Random r = new Random(System.currentTimeMillis());
        return (10000 + r.nextInt(20000));
    }

    private void initiateEmail(SendEmail sendEmail) throws Exception {
        try{
            /* Getting Template FROM DB*/
            AppConfigs appConfigs = appConfigService.getAppConfigsByName(sendEmail.getEmailTemplateName());
            String template = appConfigs.getValue();

            /* Preparing Email Object */
            String toEmail = sendEmail.getToEmail();
            String subject = sendEmail.getSubject();
            String fromEmail = emailSender;
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(fromEmail, appName);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(toEmail);

            /* Replacing email values with content */
            for(String names: sendEmail.getContent().keySet()){
                String templateVar = "[[" + names + "]]";
                template = template.replace(templateVar, sendEmail.getContent().get(names));
            }

            /* Sending Email */
            mimeMessageHelper.setText(template, true);
            mailSender.send(mimeMessage);
        }catch(Exception e){
            throw new Exception("Failed to send email");
        }
    }
}
