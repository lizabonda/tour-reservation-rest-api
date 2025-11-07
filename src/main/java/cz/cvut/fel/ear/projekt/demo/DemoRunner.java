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

        Tour tour = new Tour();
        tour.setDescription("Demo Greece Tour");
        tour.setTitle("Demo Tour");
        tour.setPrice(500.0);
        tour.setDestination("Greece");
        tour.setCapacity(3);
        tour.setStartDate(LocalDate.now().plusDays(50));
        tour.setEndDate(LocalDate.now().plusDays(57));
        tourDao.save(tour);

        Accommodation acc = new Accommodation();
        acc.setName("Demo Hotel");
        acc.setAddress("Demo Address");
        acc.setCity("Prague");
        acc.setRoomType("Single");
        acc.setPricePerNight(100.0);
        acc.setCapacity(2);
        acc.setMealPlan(MealPlan.ALL_INCLUSIVE);
        accommodationDao.save(acc);

        Reservation res = new Reservation();
        res.setStartDate(LocalDateTime.now().plusDays(50));
        res.setEndDate(LocalDateTime.now().plusDays(53));
        res.setReservationPrice(300.0);
        res.setAccomodation(acc);

        User user = new User();
        user.setFirstName("Alice");
        user.setLastName("Demo");
        user.setDateOfBirth(LocalDate.of(1995, 5, 5));
        user.setUsername("alice");
        user.setPassword("password");
        user.setPhoneNumber("123456789");
        user.setEmail("alice@example.com");
        user.setRole(Role.CUSTOMER);
        user.validateAdult();

        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setCreatedAt(LocalDate.now());

        List<Person> persons = new ArrayList<>();
        persons.add(user);
        booking.setPersons(persons);
        user.setBooking(booking);

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(res);
        booking.setReservations(reservations);
        res.setBooking(booking);

        System.out.println(booking.priceReport());

        bookingService.createBooking(booking);

        System.out.println("BOOKING SAVED, TOTAL = " + booking.getTotalPrice());

    }}
