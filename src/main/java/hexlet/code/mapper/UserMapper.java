package hexlet.code.mapper;

import hexlet.code.dto.user.UserCreateDto;
import hexlet.code.dto.user.UserResponseDto;
import hexlet.code.dto.user.UserUpdateDto;
import hexlet.code.model.User;
import org.mapstruct.*;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {JsonNullableMapper.class
        },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public abstract UserResponseDto toResponseDto(User user);

    public abstract User toEntity(UserCreateDto dto);

    @BeforeMapping
    public void beforeMapping(UserCreateDto dto) {
        String password = dto.getPassword();
        dto.setPassword(passwordEncoder.encode(password));
    }

    public abstract void update(UserUpdateDto updateDto, @MappingTarget User user);

    @BeforeMapping
    public void beforeMapping(UserUpdateDto dto) {
        if (dto.getPassword() != null && dto.getPassword().isPresent()) {
            String password = dto.getPassword().get();
            String encryptedPassword = passwordEncoder.encode(password);
            dto.setPassword(JsonNullable.of(encryptedPassword));
        }
    }
}
