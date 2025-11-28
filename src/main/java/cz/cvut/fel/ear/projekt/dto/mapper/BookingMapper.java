package cz.cvut.fel.ear.projekt.dto.mapper;

import cz.cvut.fel.ear.projekt.dto.BookingDto;
import cz.cvut.fel.ear.projekt.model.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    Booking bookingDtoToBooking(BookingDto dto);
    BookingDto  bookingToBookingDto(Booking booking);
}
