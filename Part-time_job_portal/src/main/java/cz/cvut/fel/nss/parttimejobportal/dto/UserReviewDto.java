package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class UserReviewDto {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Size(max = 255, min = 0, message = "Max 255 characters.")
    private String note;

    @NotNull(message = "Date cannot be blank")
    private LocalDateTime date;

    @Min(value = 0, message = "Min 0")
    @Max(value = 5, message = "Max 5")
    private double rating;

    @NotNull(message = "User id cannot be blank")
    private Long userId;

    private Long authorId;
    private JobSessionDto tripSessionDto;


    public UserReviewDto(@NotNull(message = "Id cannot be blank") Long id, @NotNull(message = "Note cannot be blank") @Size(max = 255, min = 0, message = "Max 255 characters.") String note,
                         @NotNull(message = "Date cannot be blank") LocalDateTime date, @Min(value = 0, message = "Min 0") @Max(value = 5, message = "Max 5") double rating,
                         @NotNull(message = "User id cannot be blank") Long userId, Long authorId, JobSessionDto tripSessionDto) {

        this.id = id;
        this.note = note;
        this.date = date;
        this.rating = rating;
        this.userId = userId;
        this.authorId = authorId;
        this.tripSessionDto = tripSessionDto;
    }


    public UserReviewDto() {

    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
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


    public Long getAuthorId() {

        return authorId;
    }


    public void setAuthorId(Long authorId) {

        this.authorId = authorId;
    }


    public JobSessionDto getTripSessionDto() {

        return tripSessionDto;
    }


    public void setTripSessionDto(JobSessionDto tripSessionDto) {

        this.tripSessionDto = tripSessionDto;
    }
}
