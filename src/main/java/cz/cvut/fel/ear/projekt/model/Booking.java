package cz.cvut.fel.ear.projekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Booking {
    private static final int EARLY_DAYS=45;
    private static final int LASTMINUTE_DAYS=3;
    private static final double LASTMINUTE_DAYS_DISCOUNT=0.15;
    private static final double EARLY_DAYS_DISCOUNT=0.1;
    private static final double ALL_INCLUSIVE_PERCENT=0.15;

    @Id
    @GeneratedValue
    private Long id;

    private int reservationNumber;
    private double totalPrice;

    @NotNull
    @Column(nullable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("startDate")
    private List<Reservation> reservations= new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "booking_person",
            joinColumns = @JoinColumn(name = "booking_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    @OrderBy("firstName, lastName")
    private List<Person> persons = new ArrayList<>();


    @ManyToOne(optional = false)
    @JoinColumn(name = "tour_id", nullable = false)
    private Tour tour;

    @ManyToMany
    @JoinTable(name="booking_activity",
            joinColumns = @JoinColumn(name = "booking_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "activity_id", nullable = false))
    private List<Activity> activities;

    @OneToMany(mappedBy = "booking")
    @OrderBy("date")
    private List<Payment> payments= new ArrayList<>();



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "reservationNumber=" + reservationNumber +
                ", totalPrice=" + totalPrice +
                '}';
    }

    public double tourPrice() {
        return getTour().getPrice();
    }

    public double accommodationPrice() {
        double accomodation_price=0.0;
        List<Reservation> reservations =getReservations();
        for (Reservation reservation : reservations) {
            accomodation_price+=reservation.getReservationPrice();
        }
        return accomodation_price;
    }

        public double basePrice() {
            return tourPrice() + accommodationPrice();
        }

    public double discount(){
        Tour tour = getTour();
        double basePrice=basePrice();
        long days = ChronoUnit.DAYS.between(getCreatedAt(), tour.getStartDate());
        double discount=0.0;
        if(days>=EARLY_DAYS){
            double erly_discount = basePrice*EARLY_DAYS_DISCOUNT;
            discount=erly_discount;
        }
        else if(days<=LASTMINUTE_DAYS){
            double last_minute_discount = basePrice*LASTMINUTE_DAYS_DISCOUNT;
            discount=last_minute_discount;
        }
        return discount;
    }

    public double charge(){
        double charge=0.0;
        List<Reservation> reservations =getReservations();
        for (Reservation reservation : reservations) {
            if(reservation.getAccomodation().getMealPlan()== MealPlan.ALL_INCLUSIVE){
                charge+= reservation.getReservationPrice()*ALL_INCLUSIVE_PERCENT;
            }
        }
        return charge;
    }

    public double totalPrice(){
        double base = basePrice();
        double discount = discount();
        double charge = charge();
        return base - discount + charge;
    }

    public void saveTotalPrice() {
        setTotalPrice(totalPrice());
    }

    public String priceReport() {
        double tour = tourPrice();
        double acc = accommodationPrice();
        double base = basePrice();
        double discount = discount();
        double charge = charge();
        double total = totalPrice();

        long days = ChronoUnit.DAYS.between(getCreatedAt(), getTour().getStartDate());

        String discountExplain;
        if (days >= EARLY_DAYS) {
            discountExplain = String.format(
                    "Early booking discount: days=%d >= %d -> discount = base(%.2f) * %.2f = %.2f",
                    days, EARLY_DAYS, base, EARLY_DAYS_DISCOUNT, discount
            );
        } else if (days <= LASTMINUTE_DAYS) {
            discountExplain = String.format(
                    "Last-minute discount: days=%d <= %d -> discount = base(%.2f) * %.2f = %.2f",
                    days, LASTMINUTE_DAYS, base, LASTMINUTE_DAYS_DISCOUNT, discount
            );
        } else {
            discountExplain = String.format(
                    "No discount applied: days=%d is between %d and %d -> discount = 0.00",
                    days, LASTMINUTE_DAYS, EARLY_DAYS
            );
        }

        return """
        -- Price breakdown for booking %d --
        Tour price:                  %.2f
        Accommodation price:         %.2f
        Base price:                  base = tour + accommodation = %.2f = %.2f + %.2f
        Days before tour start:      %d
        %s
        Extra charges (ALL_INCLUSIVE): %.2f
        Final price:                 total = base - discount + charge
                                    = %.2f - %.2f + %.2f = %.2f
        """.formatted(
                getId(),
                tour,
                acc,
                base, tour, acc,
                days,
                discountExplain,
                charge,
                base, discount, charge, total
        );
    }


}
