package cz.cvut.fel.nss.parttimejobportal.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "TripSession.findByTrip", query = "SELECT t FROM TripSession t WHERE t.trip.short_name = :trip_short_name AND t.deleted_at is null")
})
@Table(name = "TRIP_SESSION")
public class TripSession extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false)
    //todo odkomentovat
//    @FutureOrPresent
    private LocalDate from_date;

    @Basic(optional = false)
    @Column(nullable = false)
//    @FutureOrPresent
    private LocalDate to_date;

    @Basic(optional = false)
    @Column(nullable = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 99999, message = "Max 99999")
    private double price;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "tripSession")
    private List<Enrollment> enrollments;

    public TripSession() {
        this.enrollments = new ArrayList<>();
    }

    public TripSession(Trip trip, LocalDate from_date, LocalDate to_date, double price) {
        this.trip = trip;
        this.from_date = from_date;
        this.to_date = to_date;
        this.price = price;
        this.enrollments = new ArrayList<>();
    }

    public LocalDate getFrom_date() {
        return from_date;
    }

    public void setFrom_date(LocalDate from_date) {
        this.from_date = from_date;
    }

    public LocalDate getTo_date() {
        return to_date;
    }

    public void setTo_date(LocalDate to_date) {
        this.to_date = to_date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    @JsonIgnore
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

}
