package cz.cvut.fel.nss.parttimejobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "TRIP_REVIEW")
@NamedQueries({
        @NamedQuery(name = "JobReview.findByTripId", query = "SELECT t FROM Offer t WHERE t.short_name = :id AND t.deleted_at is null")
        })
public class JobReview extends AbstractEntity {

    @Basic(optional = false)
    @Size(max = 255, min = 0, message = "Max 255 characters.")
    private String note;

    @Basic(optional = false)
    @Column(nullable = false)
    private LocalDateTime date;

    @Basic(optional = false)
    @Column(nullable = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 5, message = "Max 5")
    private double rating;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User author;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Offer trip;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    public JobReview() {
        date = LocalDateTime.now();
    }

    public JobReview(@Size(max = 255, min = 0, message = "Max 255 characters.") String note, LocalDateTime date, @Min(value = 0, message = "Min 0") @Max(value = 5, message = "Max 5") double rating, User author, Offer trip, Enrollment enrollment) {
        this.note = note;
        this.date = date;
        this.rating = rating;
        this.author = author;
        this.enrollment = enrollment;
        this.trip = trip;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public AbstractUser getAuthor() {
        return author;
    }

    public Offer getTrip() {
        return trip;
    }

    public void setTrip(Offer trip) {
        this.trip = trip;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setAuthor(User author) {
        this.author = author;
        author.addJobReview(this);
    }

}
