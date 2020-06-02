package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.Basic;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

public class JobReviewDto implements Serializable {

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

    private String author;



    public JobReviewDto(@NotNull(message = "Id cannot be blank") Long id, @Size(max = 255, min = 0, message = "Max 255 characters.") String note,
                         LocalDateTime date, @Min(value = 0, message = "Min 0") @Max(value = 5, message = "Max 5") double rating, String author) {

        this.id = id;
        this.note = note;
        this.date = date;
        this.rating = rating;
        this.author = author;
    }


    public JobReviewDto() {
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
