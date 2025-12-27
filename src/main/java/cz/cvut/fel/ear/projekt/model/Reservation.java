package cz.cvut.fel.ear.projekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@NamedQuery(
        name = "Reservation.findIntersection",
        query = "SELECT r from Reservation r " +
                "WHERE r.accommodation.id = :accommodationId " +
                "AND r.endDate > :from " +
                "AND r.startDate < :to " +
                "ORDER BY r.startDate"
)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservation_seq")
    @SequenceGenerator(name = "reservation_seq", sequenceName = "reservation_seq", allocationSize = 1)
    private Long id;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDate;
    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDate;
    private double reservationPrice;

    @ManyToOne(optional = false)
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public double getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(double reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", reservationPrice=" + reservationPrice +
                ", accomodation=" + accommodation +
                '}';
    }
}
