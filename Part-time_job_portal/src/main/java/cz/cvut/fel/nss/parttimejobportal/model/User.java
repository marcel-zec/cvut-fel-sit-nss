package cz.cvut.fel.nss.parttimejobportal.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APP_USER")
@NamedQueries({
        @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email AND u.deleted_at is null")
})
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends AbstractEntity {

    @Basic(optional = false)
    @Column(nullable = false, length = 30)
    @Size(max = 30, min = 1, message = "First name is in incorrect format.")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 30, min = 1, message = "Last name is in incorrect format.")
    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 255, min = 6, message = "Password is in incorrect format.")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @Email(message = "Email should be valid")
    @Basic(optional = false)
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email cannot be blank")
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    private TravelJournal travel_journal;

    @OneToMany(mappedBy = "author")
    private List<TripReview> tripReviews;

    @OneToMany(mappedBy = "user")
    private List<UserReview> userReviews;

    @OneToMany(mappedBy = "author")
    private List<UserReview> userReviewsAuthor;

    public User() {
        this.role = Role.USER;
        this.tripReviews = new ArrayList<>();
        this.userReviews = new ArrayList<>();
        this.userReviewsAuthor = new ArrayList<>();
    }

    public User(String password, String firstName, String lastName, String email){
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userReviews = new ArrayList<>();
        this.userReviewsAuthor = new ArrayList<>();
        this.role = Role.USER;
        this.travel_journal = new TravelJournal();
        this.tripReviews = new ArrayList<>();
    }

    public User(@Email(message = "Email should be valid") String email,
                @Size(max = 255, min = 6, message = "Password is in incorrect format.") String password) {
        this.email = email;
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void erasePassword() {
        this.password = null;
    }

    public String getEmail() {
        return email;
    }

    public void encodePassword() {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setTravel_journal(TravelJournal travel_journal) {
        this.travel_journal = travel_journal;
    }

    public Address getAddress() {
        return address;
    }

    public TravelJournal getTravel_journal() {
        return travel_journal;
    }

    public List<TripReview> getTripReviews() {
        return tripReviews;
    }

    public void addTripReview(TripReview tripReview) {
         tripReviews.add(tripReview);
    }

    public void addUserReviewAutor(UserReview userReview){
        userReviewsAuthor.add(userReview);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", address=" + address +
                ", travel_journal=" + travel_journal +
                '}';
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


    public List<UserReview> getUserReviewsAuthor() {
        return userReviewsAuthor;
    }


    public void setUserReviewsAuthor(List<UserReview> userReviewsAuthor) {
        this.userReviewsAuthor = userReviewsAuthor;
    }

    public void addUserReview(UserReview userReview){
        this.userReviews.add(userReview);
    }

    public void addUserReviewAuthor(UserReview userReview){
        this.userReviewsAuthor.add(userReview);
    }
}
