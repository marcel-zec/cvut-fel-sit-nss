package cz.cvut.fel.nss.parttimejobportal.dto;

import cz.cvut.fel.nss.parttimejobportal.model.EnrollmentState;

import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

public class EnrollmentDto {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    @PastOrPresent
    private LocalDateTime enrollDate;

    @Basic(optional = false)
    private boolean deposit_was_paid;

    @Basic(optional = false)
    private int actual_xp_reward;

    @Enumerated(EnumType.STRING)
    private EnrollmentState state;

    private List<AchievementSpecialDto> recieved_achievements_special;
    private Long jobJournalId;
    private OfferDto trip;
    private JobSessionDto tripSession;
    private JobReviewDto jobReview;


    public EnrollmentDto(@NotNull(message = "Id cannot be blank") Long id, @PastOrPresent LocalDateTime enrollDate, boolean deposit_was_paid, int actual_xp_reward, EnrollmentState state, List<AchievementSpecialDto> recieved_achievements_special,
                         Long jobJournalId, OfferDto trip, JobSessionDto tripSession, JobReviewDto jobReview) {
        this.id = id;
        this.enrollDate = enrollDate;
        this.deposit_was_paid = deposit_was_paid;
        this.actual_xp_reward = actual_xp_reward;
        this.state = state;
        this.recieved_achievements_special = recieved_achievements_special;
        this.jobJournalId = jobJournalId;
        this.trip = trip;
        this.tripSession = tripSession;
        this.jobReview = jobReview;
    }


    public EnrollmentDto() {

    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }


    public LocalDateTime getEnrollDate() {

        return enrollDate;
    }


    public void setEnrollDate(LocalDateTime enrollDate) {

        this.enrollDate = enrollDate;
    }


    public boolean isDeposit_was_paid() {

        return deposit_was_paid;
    }


    public void setDeposit_was_paid(boolean deposit_was_paid) {

        this.deposit_was_paid = deposit_was_paid;
    }


    public int getActual_xp_reward() {

        return actual_xp_reward;
    }


    public void setActual_xp_reward(int actual_xp_reward) {

        this.actual_xp_reward = actual_xp_reward;
    }


    public EnrollmentState getState() {

        return state;
    }


    public void setState(EnrollmentState state) {

        this.state = state;
    }


    public List<AchievementSpecialDto> getRecieved_achievements_special() {
        return recieved_achievements_special;
    }

    public void setRecieved_achievements_special(List<AchievementSpecialDto> recieved_achievements_special) {
        this.recieved_achievements_special = recieved_achievements_special;
    }

    public Long getJobJournalId() {

        return jobJournalId;
    }


    public void setJobJournalId(Long jobJournalId) {

        this.jobJournalId = jobJournalId;
    }


    public OfferDto getTrip() {

        return trip;
    }


    public void setTrip(OfferDto trip) {

        this.trip = trip;
    }


    public JobSessionDto getTripSession() {

        return tripSession;
    }


    public void setTripSession(JobSessionDto tripSession) {

        this.tripSession = tripSession;
    }

    public JobReviewDto getJobReview() {
        return jobReview;
    }

    public void setJobReview(JobReviewDto jobReview) {
        this.jobReview = jobReview;
    }
}
