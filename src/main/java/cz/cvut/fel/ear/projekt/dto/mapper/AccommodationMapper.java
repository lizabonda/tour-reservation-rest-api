package cz.cvut.fel.ear.projekt.dto.mapper;

import cz.cvut.fel.ear.projekt.dto.AccommodationDto;
import cz.cvut.fel.ear.projekt.model.Accommodation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccommodationMapper {
    Accommodation accommodationDtoToAccommodation(AccommodationDto dto);
    AccommodationDto  accommodationToAccommodationDto(Accommodation accommodation);
}
