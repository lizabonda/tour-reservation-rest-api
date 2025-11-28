package cz.cvut.fel.ear.projekt.dto.mapper;

import cz.cvut.fel.ear.projekt.dto.PersonDto;
import cz.cvut.fel.ear.projekt.model.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper {
    Person personDtoToPerson(PersonDto dto);
    PersonDto personToPersonDto(Person person);
}
