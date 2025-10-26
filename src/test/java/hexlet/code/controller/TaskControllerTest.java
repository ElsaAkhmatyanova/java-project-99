package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.IntegrationTest;
import hexlet.code.TestModelGenerator;
import hexlet.code.component.DataInitializer;
import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@IntegrationTest
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestModelGenerator testModelGenerator;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskStatusRepository taskStatusRepository;
    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Task testTask;
    private String testUserToken;

    @BeforeEach
    void setUp() {
        dataInitializer.initializeRoles();
        dataInitializer.initializeTaskStatuses();
        testUser = Instancio.of(testModelGenerator.getUserModel()).create();
        testUserToken = jwtUtils.generateToken(testUser.getEmail(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
        userRepository.save(testUser);
        testTask = Instancio.of(testModelGenerator.getTaskModel()).create();
        testTask.setAssignee(testUser);
        testTask.setTaskStatus(taskStatusRepository.findBySlug("draft").orElse(null));
        taskRepository.save(testTask);
    }

    @Test
    void getTaskById() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findBySlug("draft").orElseGet(Assertions::fail);
        var request = get("/api/tasks/" + testTask.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testUserToken);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .and(v -> v.node("id").isEqualTo(testTask.getId()),
                        v -> v.node("index").isEqualTo(testTask.getIndex()),
                        v -> v.node("title").isEqualTo(testTask.getName()),
                        v -> v.node("content").isEqualTo(testTask.getDescription()),
                        v -> v.node("assignee_id").isEqualTo(testUser.getId()),
                        v -> v.node("status").isEqualTo(taskStatus.getSlug()),
                        v -> v.node("createdAt").isNotNull());
    }

    @Test
    void getTaskStatuses() throws Exception {
        var request = get("/api/tasks")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testUserToken);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .isArray()
                .isNotEmpty();
    }

    @Test
    void createTaskStatus() throws Exception {
        TaskStatus taskStatus = taskStatusRepository.findBySlug("published").orElseGet(Assertions::fail);
        var requestDto = new TaskCreateDto();
        requestDto.setIndex(15123);
        requestDto.setTitle("taskTitle");
        requestDto.setContent("taskContent");
        requestDto.setAssigneeId(testUser.getId());
        requestDto.setStatus(taskStatus.getSlug());
        String stringRequestBody = objectMapper.writeValueAsString(requestDto);

        var request = post("/api/tasks")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringRequestBody);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .and(v -> v.node("index").isEqualTo(requestDto.getIndex()),
                        v -> v.node("title").isEqualTo(requestDto.getTitle()),
                        v -> v.node("content").isEqualTo(requestDto.getContent()),
                        v -> v.node("assignee_id").isEqualTo(testUser.getId()),
                        v -> v.node("status").isEqualTo(taskStatus.getSlug()),
                        v -> v.node("createdAt").isNotNull());
    }

    @Test
    void updateTaskStatus() throws Exception {
        String taskStatusSlug = "to_review";
        TaskStatus newTaskStatus = taskStatusRepository.findBySlug(taskStatusSlug).orElseGet(Assertions::fail);
        String newTitle = "newName";
        String newContent = "newSlug";
        Integer newIndex = 1934;
        var requestDto = new TaskUpdateDto();
        requestDto.setTitle(JsonNullable.of(newTitle));
        requestDto.setContent(JsonNullable.of(newContent));
        requestDto.setIndex(JsonNullable.of(newIndex));
        requestDto.setStatus(JsonNullable.of(newTaskStatus.getSlug()));
        String stringRequestBody = objectMapper.writeValueAsString(requestDto);

        var request = put("/api/tasks/" + testTask.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testUserToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(stringRequestBody);
        var result = mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();
        assertThatJson(body)
                .and(v -> v.node("index").isEqualTo(requestDto.getIndex()),
                        v -> v.node("title").isEqualTo(requestDto.getTitle()),
                        v -> v.node("content").isEqualTo(requestDto.getContent()),
                        v -> v.node("assignee_id").isEqualTo(testUser.getId()),
                        v -> v.node("status").isEqualTo(newTaskStatus.getSlug()));
    }

    @Test
    void deleteTaskStatus() throws Exception {
        var request = delete("/api/tasks/" + testTask.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + testUserToken);
        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        assertThat(taskRepository.findAll()).isEmpty();
    }
}
