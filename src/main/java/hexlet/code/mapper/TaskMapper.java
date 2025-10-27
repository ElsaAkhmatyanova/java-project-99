package hexlet.code.mapper;

import hexlet.code.dto.task.TaskCreateDto;
import hexlet.code.dto.task.TaskResponseDto;
import hexlet.code.dto.task.TaskUpdateDto;
import hexlet.code.model.Task;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {JsonNullableMapper.class,
                ReferenceMapper.class,
                TaskStatusMapper.class,
                LabelMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TaskMapper {

    @Mapping(target = "assigneeId", source = "assignee.id")
    @Mapping(target = "title", source = "name")
    @Mapping(target = "content", source = "description")
    @Mapping(target = "status", source = "taskStatus.slug")
    @Mapping(target = "taskLabelIds", source = "labels", qualifiedByName = "getLabelIds")
    public abstract TaskResponseDto toResponseDto(Task taskStatus);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "getTaskStatusBySlag")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "getLabelsByIds")
    public abstract Task toEntity(TaskCreateDto dto);

    @Mapping(target = "assignee", source = "assigneeId")
    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(target = "taskStatus", source = "status", qualifiedByName = "getTaskStatusBySlag")
    @Mapping(target = "labels", source = "taskLabelIds", qualifiedByName = "getLabelsByIds")
    public abstract void update(TaskUpdateDto updateDto, @MappingTarget Task taskStatus);
}
