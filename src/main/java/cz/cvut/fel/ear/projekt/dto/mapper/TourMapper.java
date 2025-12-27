package cz.cvut.fel.ear.projekt.dto.mapper;

import cz.cvut.fel.ear.projekt.dto.TourDto;
import cz.cvut.fel.ear.projekt.model.Tour;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AccommodationMapper.class})
public interface TourMapper {
    Tour tourDtoToTour(TourDto dto);
    TourDto tourToTourDto(Tour tour);
}
