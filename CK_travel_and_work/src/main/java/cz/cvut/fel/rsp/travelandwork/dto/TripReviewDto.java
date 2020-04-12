package cz.cvut.fel.rsp.travelandwork.dto;

import javax.persistence.Basic;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class TripReviewDto {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    @Size(max = 255, min = 0, message = "Max 255 characters.")
    private String note;

    @Basic(optional = false)
    private LocalDateTime date;

    @Basic(optional = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 5, message = "Max 5")
    private double rating;

    private Long userId;
    private Long tripId;


    public TripReviewDto(@NotNull(message = "Id cannot be blank") Long id, @Size(max = 255, min = 0, message = "Max 255 characters.") String note,
                         LocalDateTime date, @Min(value = 0, message = "Min 0") @Max(value = 5, message = "Max 5") double rating,
                         Long userId, Long tripId) {

        this.id = id;
        this.note = note;
        this.date = date;
        this.rating = rating;
        this.userId = userId;
        this.tripId = tripId;
    }


    public TripReviewDto() {
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


    public Long getUserId() {

        return userId;
    }


    public void setUserId(Long userId) {

        this.userId = userId;
    }


    public Long getTripId() {

        return tripId;
    }


    public void setTripId(Long tripId) {

        this.tripId = tripId;
    }
}
