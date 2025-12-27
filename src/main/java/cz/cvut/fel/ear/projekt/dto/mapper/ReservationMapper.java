package cz.cvut.fel.ear.projekt.dto.mapper;

import cz.cvut.fel.ear.projekt.dto.ReservationDto;
import cz.cvut.fel.ear.projekt.model.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AccommodationMapper.class})
public interface ReservationMapper {
    Reservation reservationDtoToReservation(ReservationDto dto);

    @Mapping(target = "bookingId",  source = "booking.id")
    ReservationDto reservationToReservationDto(Reservation reservation);


}
