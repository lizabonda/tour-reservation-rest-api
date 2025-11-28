package cz.cvut.fel.ear.projekt.service;

import cz.cvut.fel.ear.projekt.dao.AccommodationDao;
import cz.cvut.fel.ear.projekt.dao.BookingDao;
import cz.cvut.fel.ear.projekt.dao.ReservationDao;
import cz.cvut.fel.ear.projekt.dao.TourDao;
import cz.cvut.fel.ear.projekt.dto.BookingDto;
import cz.cvut.fel.ear.projekt.dto.ReservationDto;
import cz.cvut.fel.ear.projekt.dto.mapper.BookingMapper;
import cz.cvut.fel.ear.projekt.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final ReservationDao reservationDao;
    private final BookingMapper bookingMapper;
    private final TourDao tourDao;
    private final AccommodationDao accommodationDao;



    public BookingService(BookingDao bookingDao, TourService tourService, ReservationDao reservationDao, BookingMapper bookingMapper, TourDao tourDao, AccommodationDao accommodationDao) {
        this.bookingDao = bookingDao;
        this.tourService = tourService;
        this.reservationDao = reservationDao;
        this.bookingMapper = bookingMapper;
        this.tourDao = tourDao;
        this.accommodationDao = accommodationDao;
    }

    public void createBooking(Booking booking) {
        List<Reservation> reservations=booking.getReservations();
        Tour tour = booking.getTour();
        if (tour == null) {
            throw new IllegalArgumentException("Booking must have a tour");
        }
        //tour capacity validation
        int requestedSize = booking.getPersons().size();
        tourService.validateCapacity(tour, requestedSize);
        // accommodation validation
        for(Reservation r:reservations){
            List<Reservation> intersection= new ArrayList<>();
            intersection.addAll(reservationDao.findIntersection(r.getAccomodation().getId(), r.getStartDate(),r.getEndDate()));
            if (!intersection.isEmpty()) {
                throw new IllegalStateException("Accommodation is not available for given dates");
            }
        }
        // calculating price
        booking.saveTotalPrice();
        bookingDao.save(booking);
    }

    public List<BookingDto> getBookingsCreatedBetween(LocalDate fromDate, LocalDate toDate) {
        List<Booking> bookings=bookingDao.findBookingsCreatedBetween(fromDate, toDate);
        List<BookingDto> bookingDtos=new ArrayList<>();
        for (Booking booking:bookings) {
            bookingDtos.add(bookingMapper.bookingToBookingDto(booking));
        }
        return bookingDtos;
    }

    public void updateBookingAccommodation(Long reservationId, Long newaccommodationId) {

        Reservation r = reservationDao.find(reservationId);
        if (r == null) {
            throw new IllegalArgumentException("Reservation not found");
        }
        Booking booking= r.getBooking();

        Accommodation newAccommodation= accommodationDao.find(newaccommodationId);
        if (newAccommodation == null) {
            throw new IllegalArgumentException("Accommodation not found");
        }

        List<Reservation> intersection= reservationDao.findIntersection(newaccommodationId, r.getStartDate(),r.getEndDate());
        Reservation toDelete=null;
        for (Reservation reservation:intersection) {
            if(reservation.getId().equals(reservationId)) {
                toDelete=reservation;
                break;
            }
        }
        intersection.remove(toDelete);
        if (!intersection.isEmpty()) {
            throw new IllegalStateException("Accommodation is not available for given dates");
        }

        r.setAccomodation(newAccommodation);
        booking.saveTotalPrice();
        bookingDao.save(booking);
        }

    public void removeBookingByTour(String destination, LocalDate startDate) {
        Tour tour= tourDao.findByDestinationAndStartDate(destination, startDate);
        List<Booking> copy_bookings=new ArrayList<>(tour.getBookings());

        for (Booking booking:copy_bookings) {
            tour.removeBooking(booking);
            bookingDao.remove(booking);
        }
    }

    }




