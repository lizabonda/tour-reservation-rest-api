package cz.cvut.fel.ear.projekt.dto;

import cz.cvut.fel.ear.projekt.model.Accommodation;
import cz.cvut.fel.ear.projekt.model.Booking;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ReservationDto(Long id,
                             LocalDate startDate,
                             LocalDate endDate,
                             double reservationPrice,
                             AccommodationDto accommodation,
                             Long bookingId) {}
