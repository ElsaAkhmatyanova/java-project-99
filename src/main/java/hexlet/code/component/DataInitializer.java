package hexlet.code.component;

import hexlet.code.model.User;
import hexlet.code.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CustomUserDetailsService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeUser();
    }

    private void initializeUser() {
        String email = "hexlet@example.com";
        String password = "qwerty";
        if (!userService.userExists(email)) {
            var user = new User();
            user.setEmail(email);
            user.setPasswordDigest(password);
            userService.createUser(user);
        }
    }
}
