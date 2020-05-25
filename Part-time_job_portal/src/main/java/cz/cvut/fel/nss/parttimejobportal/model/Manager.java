package cz.cvut.fel.nss.parttimejobportal.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "APP_MANAGER")
@NamedQueries({
        @NamedQuery(name = "Manager.findByEmail", query = "SELECT m FROM Manager m WHERE m.email = :email AND m.deleted_at is null")
})
public class Manager extends AbstractUser {

    @OneToMany(mappedBy = "author")
    private List<UserReview> userReviewsAuthor;


    public Manager() {
        super(Role.ADMIN);
        userReviewsAuthor = new ArrayList<>();
    }

    public Manager(String password, String firstName, String lastName, String email) {

        super(password,firstName,lastName, email, Role.ADMIN);
        userReviewsAuthor = new ArrayList<>();
    }


    public List<UserReview> getUserReviewsAuthor() {

        return userReviewsAuthor;
    }


    public void setUserReviewsAuthor(List<UserReview> userReviewsAuthor) {

        this.userReviewsAuthor = userReviewsAuthor;
    }

    public void addUserReviewAuthor(UserReview userReview){
        userReviewsAuthor.add(userReview);
    }
}
