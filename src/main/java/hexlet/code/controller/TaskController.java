package hexlet.code.controller;

import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskFiltrationDto;
import hexlet.code.dto.task.TaskResponseDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    public TaskResponseDto getTask(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAllTasks(@ParameterObject TaskFiltrationDto filtration) {
        List<TaskResponseDto> responseDtoList = taskService.getAllTasks(filtration);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponseDto createTask(@Valid @RequestBody TaskCreateDto dto) {
        return taskService.createTask(dto);
    }

    @PutMapping("/{id}")
    public TaskResponseDto updateTask(@PathVariable Long id,
                                      @Valid @RequestBody TaskUpdateDto dto) {
        return taskService.updateTask(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
