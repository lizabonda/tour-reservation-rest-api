package cz.cvut.fel.ear.projekt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Tour {
    @Id
    @GeneratedValue
    private Long id;
    @NotNull
    @Column(nullable = false)
    private String title;
    @NotNull
    @Column(nullable = false)
    private String destination;
    @NotNull
    @Column(nullable = false)
    private LocalDate startDate;
    @NotNull
    @Column(nullable = false)
    private LocalDate endDate;
    @NotNull
    @Column(nullable = false)
    private String description;
    private int capacity;
    private double price;

    @ManyToMany
    @JoinTable(name="tour_accomodation",
    joinColumns = @JoinColumn(name = "tour_id", nullable = false),
    inverseJoinColumns = @JoinColumn(name = "accomodation_id", nullable = false))
    private List<Accommodation> accommodations;

    @OneToMany(mappedBy = "tour")
    private List<Booking> bookings= new ArrayList<>();

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trip> trips= new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Accommodation> getAccomodations() {
        return accommodations;
    }

    public void setAccomodations(List<Accommodation> accommodations) {
        this.accommodations = accommodations;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "Tour{" +
                "capacity=" + capacity +
                ", endDate=" + endDate +
                ", startDate=" + startDate +
                ", destination='" + destination + '\'' +
                '}';
    }


}

