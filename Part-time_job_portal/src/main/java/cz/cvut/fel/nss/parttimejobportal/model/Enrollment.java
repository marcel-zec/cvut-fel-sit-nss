package cz.cvut.fel.nss.parttimejobportal.model;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ENROLLMENT")
public class Enrollment extends AbstractEntity {
    @Basic(optional = false)
    @Column(nullable = false)
    @PastOrPresent
    private LocalDateTime enrollDate;

    @Basic(optional = false)
    @Column(nullable = false)
    private boolean deposit_was_paid;

    @Basic(optional = false)
    @Column(nullable = false)
    private int actual_xp_reward;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnrollmentState state;

    @ManyToMany
    @JoinTable(
            name = "recieved_achievement_special_trip",
            joinColumns = @JoinColumn(name = "enrollment_id"),
            inverseJoinColumns = @JoinColumn(name = "achievement_special_id"))
    private List<AchievementSpecial> recieved_achievements_special;

    @ManyToOne( optional = false)
    @JoinColumn(name = "jobJournal_id", nullable = false)
    private JobJournal jobJournal;

    @ManyToOne( optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Offer trip;

    @ManyToOne( optional = false)
    @JoinColumn(name = "tripSession_id", nullable = false)
    private JobSession tripSession;


    @OneToOne(mappedBy = "enrollment")
    private JobReview jobReview;

    public LocalDateTime getEnrollDate() {
        return enrollDate;
    }

    public boolean isDeposit_was_paid() {
        return deposit_was_paid;
    }

    public int getActual_xp_reward() {
        return actual_xp_reward;
    }

    public JobJournal getJobJournal() {
        return jobJournal;
    }

    public Offer getTrip() {
        return trip;
    }

    public JobSession getTripSession() {
        return tripSession;
    }

    public void setEnrollDate(LocalDateTime enrollDate) {
        this.enrollDate = enrollDate;
    }

    public void setDeposit_was_paid(boolean deposit_was_paid) {
        this.deposit_was_paid = deposit_was_paid;
    }

    public void setActual_xp_reward(int actual_xp_reward) {
        this.actual_xp_reward = actual_xp_reward;
    }

    public void setJobJournal(JobJournal jobJournal) {
        this.jobJournal = jobJournal;
    }

    public void setTrip(Offer trip) {
        this.trip = trip;
    }

    public void setTripSession(JobSession tripSession) {
        this.tripSession = tripSession;
    }

    public EnrollmentState getState() {
        return state;
    }

    public void setState(EnrollmentState state) {
        this.state = state;
    }

    public boolean isActive(){
        return this.getState() == EnrollmentState.ACTIVE;
    }

    public boolean isFinished(){
        return this.getState() == EnrollmentState.FINISHED;
    }

    public boolean isCancelled(){
        return this.getState() == EnrollmentState.CANCELED;
    }

    public List<AchievementSpecial> getRecieved_achievements() {
        return recieved_achievements_special;
    }

    public JobReview getJobReview() {
        return jobReview;
    }

    public void setJobReview(JobReview jobReview) {
        this.jobReview = jobReview;
    }

    public boolean hasJobReview() {
        return jobReview != null;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollDate=" + enrollDate +
                ", deposit_was_paid=" + deposit_was_paid +
                ", actual_xp_reward=" + actual_xp_reward +
                ", state=" + state +
                ", recieved_achievements_special=" + recieved_achievements_special +
                ", jobJournal=" + jobJournal +
                ", trip=" + trip +
                ", tripSession=" + tripSession +
                "} " + super.toString();
    }


    public void setRecieved_achievements_special(List<AchievementSpecial> recieved_achievements_special) {

        this.recieved_achievements_special = recieved_achievements_special;
    }
}
