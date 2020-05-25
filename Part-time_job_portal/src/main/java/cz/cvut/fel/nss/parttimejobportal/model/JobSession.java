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
        @NamedQuery(name = "JobSession.findByTrip", query = "SELECT t FROM JobSession t WHERE t.trip.short_name = :trip_short_name AND t.deleted_at is null")
})
@Table(name = "JOB_SESSION")
public class JobSession extends AbstractEntity {

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
    @Min(value = 1, message = "Min 1")
    @Max(value = 500, message = "Max 500")
    private int capacity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Offer trip;

    @OneToMany(mappedBy = "tripSession")
    private List<Enrollment> enrollments;

    public JobSession() {
        this.enrollments = new ArrayList<>();
    }

    public JobSession(Offer trip, LocalDate from_date, LocalDate to_date, int capacity) {
        this.trip = trip;
        this.from_date = from_date;
        this.to_date = to_date;
        this.capacity = capacity;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Offer getTrip() {
        return trip;
    }

    public void setTrip(Offer trip) {
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
