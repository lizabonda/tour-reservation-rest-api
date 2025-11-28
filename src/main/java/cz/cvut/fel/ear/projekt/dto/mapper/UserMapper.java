package cz.cvut.fel.ear.projekt.dto.mapper;

import cz.cvut.fel.ear.projekt.dto.UserDto;
import cz.cvut.fel.ear.projekt.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto dto);
    UserDto userToUserDto(User user);
}
