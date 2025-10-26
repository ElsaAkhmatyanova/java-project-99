package hexlet.code.component;

import hexlet.code.exception.NotFoundException;
import hexlet.code.model.Role;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.RoleRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.service.CustomUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final CustomUserDetailsService customUserDetailsService;
    private final RoleRepository roleRepository;
    private final TaskStatusRepository taskStatusRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        initializeRoles();
        initializeUser();
        initializeTaskStatuses();
    }

    @Transactional
    public void initializeTaskStatuses() {
        Map<String, String> defaultTaskStatuses = Map.of(
                "Draft", "draft",
                "ToReview", "to_review",
                "ToBeFixed", "to_be_fixed",
                "ToPublish", "to_publish",
                "Published", "published");
        defaultTaskStatuses.forEach(this::createTaskStatusIfNotExist);
    }

    @Transactional
    public void initializeRoles() {
        createRoleIfNotExists(Role.ADMIN);
        createRoleIfNotExists(Role.USER);
    }

    @Transactional
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

    private void createTaskStatusIfNotExist(String name, String slug) {
        if (!taskStatusRepository.existsByNameIgnoreCaseOrSlugIgnoreCase(name, slug)) {
            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setName(name);
            taskStatus.setSlug(slug);
            taskStatusRepository.save(taskStatus);
        }
    }
}
