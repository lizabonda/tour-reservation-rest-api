package cz.cvut.fel.ear.projekt.service;

import cz.cvut.fel.ear.projekt.dao.BookingDao;
import cz.cvut.fel.ear.projekt.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@Transactional
public class BookingService {

    private final BookingDao bookingDao;
    private final TourService tourService;

    public BookingService(BookingDao bookingDao, TourService tourService) {
        this.bookingDao = bookingDao;
        this.tourService = tourService;
    }

    public void createBooking(Booking booking) {
        Objects.requireNonNull(booking);
        Tour tour = booking.getTour();
        if (tour == null) {
            throw new IllegalArgumentException("Booking must have a tour");
        }
        int requestedSize = booking.getPersons().size();
        tourService.validateCapacity(tour, requestedSize);
        booking.saveTotalPrice();
        bookingDao.save(booking);
    }
}

