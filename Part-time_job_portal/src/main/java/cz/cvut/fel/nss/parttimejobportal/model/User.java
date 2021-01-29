package cz.cvut.fel.nss.parttimejobportal.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APP_USER")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email AND u.deleted_at is null")
})
public class User extends AbstractUser {

    @Basic(optional = false)
    @Column(nullable = false, unique = true)
    @Size(max = 12, min = 9, message = "Phone number is in incorrect format.")
    @NotBlank(message = "Phone number cannot be blank")
    private String phone_number;

    @OneToOne(cascade = CascadeType.ALL)
    private JobJournal travel_journal;

    @OneToMany(mappedBy = "author")
    private List<JobReview> tripReviews;

    @OneToMany(mappedBy = "user")
    private List<UserReview> userReviews;

    public User() {

        super(Role.USER);
        this.travel_journal = new JobJournal();
        this.userReviews = new ArrayList<>();
        this.tripReviews = new ArrayList<>();
    }


    public User(String password, String firstName, String lastName, String email, String phone_number) {

        super(password,firstName,lastName, email, Role.USER);
        this.travel_journal = new JobJournal();
        this.userReviews = new ArrayList<>();
        this.tripReviews = new ArrayList<>();
        this.phone_number = phone_number;
    }


    public JobJournal getTravel_journal() {

        return travel_journal;
    }


    public void setTravel_journal(JobJournal travel_journal) {

        this.travel_journal = travel_journal;
    }


    public List<JobReview> getJobReviews() {

        return tripReviews;
    }


    public void setJobReviews(List<JobReview> tripReviews) {

        this.tripReviews = tripReviews;
    }


    public List<UserReview> getUserReviews() {

        return userReviews;
    }


    public void setUserReviews(List<UserReview> userReviews) {

        this.userReviews = userReviews;
    }

    public void addJobReview(JobReview tripReview) {
        tripReviews.add(tripReview);
    }

    public void addUserReview(UserReview userReview){
        this.userReviews.add(userReview);
    }


    public String getPhone_number() {

        return phone_number;
    }


    public void setPhone_number(String phone_number) {

        this.phone_number = phone_number;
    }


}
