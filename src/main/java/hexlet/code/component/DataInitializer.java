package hexlet.code.component;

import hexlet.code.exception.NotFoundException;
import hexlet.code.model.Role;
import hexlet.code.model.User;
import hexlet.code.repository.RoleRepository;
import hexlet.code.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeRoles();
        initializeUser();
    }

    public void initializeRoles() {
        createRoleIfNotExists(Role.ADMIN);
        createRoleIfNotExists(Role.USER);
    }

    public void initializeUser() {
        String email = "hexlet@example.com";
        String password = "qwerty";
        if (!customUserDetailsService.userExists(email)) {
            var adminRole = roleRepository.findByAuthority(Role.ADMIN)
                    .orElseThrow(() -> new NotFoundException("Role " + Role.ADMIN + " not found!"));
            var user = new User();
            user.setEmail(email);
            user.setPasswordDigest(password);
            user.setRoles(Set.of(adminRole));
            customUserDetailsService.createUser(user);
        }
    }

    private void createRoleIfNotExists(String authority) {
        if (!roleRepository.existsByAuthority(authority)) {
            var role = new Role();
            role.setAuthority(authority);
            roleRepository.save(role);
        }
    }
}
