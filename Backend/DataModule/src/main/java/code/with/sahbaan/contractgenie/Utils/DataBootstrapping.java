package code.with.sahbaan.contractgenie.Utils;

import code.with.sahbaan.contractgenie.Entities.AppConfigs;
import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Repositories.AppConfigRepository;
import code.with.sahbaan.contractgenie.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@Slf4j
public class DataBootstrapping implements CommandLineRunner {


    @Autowired
    private AppConfigRepository appConfigRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        log.info("*** INSERTING EMAIL OTP TEMPLATE ***");
        if (appConfigRepository.findByName(Constants.EMAIL_OTP_TEMPLATE_NAME) == null){
            AppConfigs appConfigs = new AppConfigs();
            appConfigs.setName(Constants.EMAIL_OTP_TEMPLATE_NAME);
            appConfigs.setValue(Constants.EMAIL_OTP_TEMPLATE);
            appConfigRepository.save(appConfigs);
        }

        log.info("*** INSERTING FORGOT PASSWORD OTP TEMPLATE ***");
        if (appConfigRepository.findByName(Constants.FORGOT_PASSWORD_OTP_TEMPLATE_NAME) == null){
            AppConfigs appConfigs = new AppConfigs();
            appConfigs.setName(Constants.FORGOT_PASSWORD_OTP_TEMPLATE_NAME);
            appConfigs.setValue(Constants.FORGOT_PASSWORD_OTP_TEMPLATE);
            appConfigRepository.save(appConfigs);
        }

        log.info("*** INSERTING SAMPLE USER ***");
        if (userRepository.findByEmail("email2@gmail.com").isEmpty()){
            Users users2 = new Users();
            users2.setFullName("Sahbaan Alam");
            users2.setIsActive(true);
            users2.setEmail("email2@@gmail.com");
            users2.setPassword(new BCryptPasswordEncoder().encode("123456789"));
            userRepository.save(users2);
        }

    }
}
