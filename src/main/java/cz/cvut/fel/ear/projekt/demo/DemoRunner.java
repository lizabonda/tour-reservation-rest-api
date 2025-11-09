package cz.cvut.fel.ear.projekt.demo;

import cz.cvut.fel.ear.projekt.dao.AccommodationDao;
import cz.cvut.fel.ear.projekt.dao.TourDao;
import cz.cvut.fel.ear.projekt.dao.UserDao;
import cz.cvut.fel.ear.projekt.model.*;
import cz.cvut.fel.ear.projekt.service.BookingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DemoRunner implements CommandLineRunner {

    private final BookingService bookingService;
    private final TourDao tourDao;
    private final UserDao userDao;
    private final AccommodationDao accommodationDao;

    public DemoRunner(BookingService bookingService,
                      TourDao tourDao,
                      UserDao userDao,
                      AccommodationDao accommodationDao) {
        this.bookingService = bookingService;
        this.tourDao = tourDao;
        this.userDao = userDao;
        this.accommodationDao = accommodationDao;
    }

    @Override
    @Transactional
    public void run(String... args) {

        Tour tour = tourDao.find(1L);

        Accommodation acc = accommodationDao.find(1L);

        User user = userDao.find(1L);

        Reservation res = new Reservation();
        res.setStartDate(LocalDateTime.now().plusDays(50));
        res.setEndDate(LocalDateTime.now().plusDays(53));
        res.setReservationPrice(300.0);
        res.setAccomodation(acc);

        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setCreatedAt(LocalDate.now());

        List<Person> persons = new ArrayList<>();
        persons.add(user);
        booking.setPersons(persons);
        user.getBookings().add(booking);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(res);
        booking.setReservations(reservations);
        res.setBooking(booking);

        System.out.println(booking.priceReport());

        bookingService.createBooking(booking);

        System.out.println("BOOKING SAVED, TOTAL = " + booking.getTotalPrice());

    }}
