package cz.cvut.fel.ear.projekt.dto;

import java.time.LocalDate;
import java.util.List;

public record BookingDto(Long id,
                         Integer reservationNumber,
                         TourDto tour,
                         List<PersonDto> persons,
                         List<ReservationDto> reservations,
                         double totalPrice){}
