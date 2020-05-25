package cz.cvut.fel.nss.parttimejobportal.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APP_USER")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email AND u.deleted_at is null")
})
public class User extends AbstractUser {


    @OneToOne(cascade = CascadeType.ALL)
    private TravelJournal travel_journal;

    @OneToMany(mappedBy = "author")
    private List<TripReview> tripReviews;

    @OneToMany(mappedBy = "user")
    private List<UserReview> userReviews;

    public User() {

        super(Role.USER);
        this.travel_journal = new TravelJournal();
        this.userReviews = new ArrayList<>();
        this.tripReviews = new ArrayList<>();
    }


    public User(String password, String firstName, String lastName, String email) {

        super(password,firstName,lastName, email, Role.USER);
        this.travel_journal = new TravelJournal();
        this.userReviews = new ArrayList<>();
        this.tripReviews = new ArrayList<>();
    }


    public TravelJournal getTravel_journal() {

        return travel_journal;
    }


    public void setTravel_journal(TravelJournal travel_journal) {

        this.travel_journal = travel_journal;
    }


    public List<TripReview> getTripReviews() {

        return tripReviews;
    }


    public void setTripReviews(List<TripReview> tripReviews) {

        this.tripReviews = tripReviews;
    }


    public List<UserReview> getUserReviews() {

        return userReviews;
    }


    public void setUserReviews(List<UserReview> userReviews) {

        this.userReviews = userReviews;
    }

    public void addTripReview(TripReview tripReview) {
        tripReviews.add(tripReview);
    }

    public void addUserReview(UserReview userReview){
        this.userReviews.add(userReview);
    }
}
