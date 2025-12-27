package cz.cvut.fel.ear.projekt.service;

import cz.cvut.fel.ear.projekt.dao.AccommodationDao;
import cz.cvut.fel.ear.projekt.dao.BookingDao;
import cz.cvut.fel.ear.projekt.dao.PersonDao;
import cz.cvut.fel.ear.projekt.dao.ReservationDao;
import cz.cvut.fel.ear.projekt.dao.TourDao;
import cz.cvut.fel.ear.projekt.dto.BookingDto;
import cz.cvut.fel.ear.projekt.dto.PersonDto;
import cz.cvut.fel.ear.projekt.dto.ReservationDto;
import cz.cvut.fel.ear.projekt.dto.TourDto;
import cz.cvut.fel.ear.projekt.dto.mapper.BookingMapper;
import cz.cvut.fel.ear.projekt.model.*;
import cz.cvut.fel.ear.projekt.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Objects;


@Service
@Transactional
public class BookingService {

    private static final Logger log = LoggerFactory.getLogger(BookingService.class);

    private final BookingDao bookingDao;
    private final TourService tourService;
    private final ReservationDao reservationDao;
    private final BookingMapper bookingMapper;
    private final TourDao tourDao;
    private final AccommodationDao accommodationDao;
    private final PersonDao personDao;



    public BookingService(BookingDao bookingDao,
                          TourService tourService,
                          ReservationDao reservationDao,
                          BookingMapper bookingMapper,
                          TourDao tourDao,
                          AccommodationDao accommodationDao,
                          PersonDao personDao) {
        this.bookingDao = bookingDao;
        this.tourService = tourService;
        this.reservationDao = reservationDao;
        this.bookingMapper = bookingMapper;
        this.tourDao = tourDao;
        this.accommodationDao = accommodationDao;
        this.personDao = personDao;
    }

    public Booking createBookingFromDto(BookingDto dto) {
        Objects.requireNonNull(dto);

        if (dto.id() != null) {
            throw new IllegalArgumentException("Booking id must be null on create");
        }
        if (dto.reservationNumber() != null) {
            throw new IllegalArgumentException("Booking reservationNumber must be null on create");
        }

        final Long tourId = Optional.ofNullable(dto.tour())
                .map(TourDto::id)
                .orElse(null);
        if (tourId == null) {
            throw new IllegalArgumentException("Booking must have tour.id");
        }

        if (dto.persons() == null || dto.persons().isEmpty()) {
            throw new IllegalArgumentException("Booking must have at least one person");
        }
        if (dto.reservations() == null || dto.reservations().isEmpty()) {
            throw new IllegalArgumentException("Booking must have at least one reservation");
        }

        final Tour tour = tourDao.find(tourId);
        if (tour == null) {
            throw new NotFoundException("Tour not found: " + tourId);
        }

        final Booking booking = new Booking();
        booking.setTour(tour);
        booking.setCreatedAt(LocalDate.now());


        final List<Person> persons = foundOrCreatePersons(dto.persons());
        booking.setPersons(persons);
        for (Person person : persons) {
            person.getBookings().add(booking);
        }

        // tour capacity validation
        int requestedSize = booking.getPersons().size();
        tourService.validateCapacity(tour, requestedSize);

        final List<Reservation> reservations = createReservations(dto.reservations(), booking);
        booking.setReservations(reservations);
        // accommodation validation + required reservation fields + bidirectional link
        for (Reservation reservation : reservations) {
            validationForReservation(booking, reservation);
        }

        if (booking.getReservationNumber() <= 0) {
            booking.setReservationNumber(bookingDao.nextReservationNumber());
        }

        log.info("Total price:\n{}", booking.priceReport());
        booking.saveTotalPrice();
        bookingDao.save(booking);

        return booking;
    }

    private List<Reservation> createReservations(List<ReservationDto> reservationsDto, Booking booking) {
        final List<Reservation> reservations = new ArrayList<>(reservationsDto.size());
        for (var reservationDto : reservationsDto) {
            if (reservationDto == null) {
                throw new IllegalArgumentException("Reservation must not be null");
            }
            if (reservationDto.id() != null) {
                throw new IllegalArgumentException("Reservation id must be null on create");
            }
            if (reservationDto.bookingId() != null) {
                throw new IllegalArgumentException("Reservation bookingId must be null on create");
            }
            if (reservationDto.startDate() == null || reservationDto.endDate() == null) {
                throw new IllegalArgumentException("Reservation must have startDate and endDate");
            }
            if (reservationDto.accommodation() == null || reservationDto.accommodation().id() == null) {
                throw new IllegalArgumentException("Reservation must have accommodation.id");
            }
            final Accommodation accommodation = accommodationDao.find(reservationDto.accommodation().id());
            if (accommodation == null) {
                throw new NotFoundException("Accommodation not found: " + reservationDto.accommodation().id());
            }

            final Reservation reservation = new Reservation();
            reservation.setStartDate(reservationDto.startDate().atStartOfDay());
            reservation.setEndDate(reservationDto.endDate().atStartOfDay());
            reservation.setReservationPrice(reservationDto.reservationPrice());
            reservation.setAccommodation(accommodation);
            reservation.setBooking(booking);
            reservations.add(reservation);
        }
        return reservations;
    }

    // We support 2 modes:
    // 1) person.id is set -> use existing Person
    // 2) person.id = null -> create new Person using (firstName, lastName, dateOfBirth)
    private List<Person> foundOrCreatePersons(List<PersonDto> personsDto) {
        final List<Person> persons = new ArrayList<>(personsDto.size());
        for (var personDto : personsDto) {
            if (personDto == null) {
                throw new IllegalArgumentException("Person must not be null");
            }

            if (personDto.id() != null) {
                final Person person = personDao.find(personDto.id());
                if (person == null) {
                    throw new NotFoundException("Person not found: " + personDto.id());
                }
                persons.add(person);
                continue;
            }

            if (personDto.firstName() == null || personDto.firstName().isBlank()) {
                throw new IllegalArgumentException("Person must have firstName when id is not provided");
            }
            if (personDto.lastName() == null || personDto.lastName().isBlank()) {
                throw new IllegalArgumentException("Person must have lastName when id is not provided");
            }
            if (personDto.dateOfBirth() == null) {
                throw new IllegalArgumentException("Person must have dateOfBirth when id is not provided");
            }

            final Person person = new Person();
            person.setFirstName(personDto.firstName());
            person.setLastName(personDto.lastName());
            person.setDateOfBirth(personDto.dateOfBirth());
            personDao.save(person);
            persons.add(person);
        }
        return persons;
    }

    public Booking findById(Long id) {
        Objects.requireNonNull(id);
        final Booking booking = bookingDao.find(id);
        if (booking == null) {
            throw new NotFoundException("Booking not found: " + id);
        }
        return booking;
    }

    private void validationForReservation(Booking booking, Reservation r) {
        if (r.getAccommodation() == null) {
            throw new IllegalArgumentException("Reservation must have accommodation");
        }
        if (r.getStartDate() == null || r.getEndDate() == null) {
            throw new IllegalArgumentException("Reservation must have startDate and endDate");
        }
        if (r.getBooking() == null) {
            r.setBooking(booking);
        }

        List<Reservation> intersection = reservationDao.findIntersection(
                r.getAccommodation().getId(),
                r.getStartDate(),
                r.getEndDate()
        );
        if (!intersection.isEmpty()) {
            throw new IllegalStateException("Accommodation is not available for given dates");
        }
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

        r.setAccommodation(newAccommodation);
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




