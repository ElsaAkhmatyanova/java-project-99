package hexlet.code.mapper;

import hexlet.code.dto.user.UserRequestDto;
import hexlet.code.dto.user.UserResponseDto;
import hexlet.code.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserResponseDto toResponseDto(User user);

    User toEntity(UserRequestDto dto);

    void updateUserFromDto(UserRequestDto dto, @MappingTarget User user);
}
