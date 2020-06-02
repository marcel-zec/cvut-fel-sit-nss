package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

public class OfferDto {

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    @Size(max = 255, min = 3, message = "Name has to be from 3 to 255 characters.")
    @NotNull(message = "Name has to be from 3 to 255 characters.")
    private String name;

    @Basic(optional = false)
    @Size(max = 100, min = 3, message = "Short name has to be from 3 to 100 characters.")
    @NotNull(message = "Short name has to be from 3 to 100 characters.")
    private String short_name;

    @Basic(optional = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 20, message = "Max 20")
    private int possible_xp_reward;

    @Basic(optional = false)
    @Size(max = 3000, min = 0, message = "Max 3000 characters.")
    private String description;

    @Basic(optional = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 5, message = "Max 5")
    private double rating;

    @Basic(optional = false)
    @Min(value = 0, message = "Min 0")
    @Max(value = 10000, message = "Max 10 000")
    private double salary;

    @Basic(optional = false)
    @Column(nullable = false)
    @Size(max = 200, min = 0, message = "Max 200 characters.")
    private String location;

    @Basic(optional = false)
    @Min(value = 0, message = "Min 0")
    private int required_level;

    private CategoryDto category;
    private Long authorId;
    private List<AchievementCertificateDto> required_achievements_certificate;
    private List<AchievementCategorizedDto> required_achievements_categorized;
    private List<AchievementSpecialDto> required_achievements_special;
    private List<AchievementSpecialDto> gain_achievements_special;
    private List<JobSessionDto> sessions;
    private List<JobReviewDto> jobReviewDtos;

    public OfferDto() {
    }

    public OfferDto(@NotNull(message = "Id cannot be blank") Long id, @Size(max = 255, min = 3, message = "Name has to be from 3 to 255 characters.") @NotNull(message = "Name has to be from 3 to 255 characters.") String name, @Size(max = 100, min = 3, message = "Short name has to be from 3 to 100 characters.") @NotNull(message = "Short name has to be from 3 to 100 characters.") String short_name,
                   @Min(value = 0, message = "Min 0") @Max(value = 20, message = "Max 20") int possible_xp_reward, @Size(max = 3000, min = 0, message = "Max 3000 characters.") String description, @Min(value = 0, message = "Min 0") @Max(value = 5, message = "Max 5") double rating,
                   @Min(value = 0, message = "Min 0") @Max(value = 10000, message = "Max 10 000") double salary, @Size(max = 200, min = 0, message = "Max 200 characters.") String location, @Min(value = 0, message = "Min 0") int required_level, CategoryDto categoryDto, Long authorId, List<AchievementCertificateDto> required_certificates,
                   List<AchievementCategorizedDto> required_achievements_categorized, List<AchievementSpecialDto> required_achievements_special,
                   List<AchievementSpecialDto> gain_achievements, List<JobSessionDto> sessions, List<JobReviewDto> jobReviewDtos) {
        this.id = id;
        this.name = name;
        this.short_name = short_name;
        this.possible_xp_reward = possible_xp_reward;
        this.description = description;
        this.rating = rating;
        this.salary = salary;
        this.location = location;
        this.required_level = required_level;
        this.category = categoryDto;
        this.required_achievements_certificate = required_certificates;
        this.required_achievements_categorized = required_achievements_categorized;
        this.required_achievements_special = required_achievements_special;
        this.gain_achievements_special = gain_achievements;
        this.sessions = sessions;
        this.jobReviewDtos = jobReviewDtos;
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public int getPossible_xp_reward() {
        return possible_xp_reward;
    }

    public void setPossible_xp_reward(int possible_xp_reward) {
        this.possible_xp_reward = possible_xp_reward;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRequired_level() {
        return required_level;
    }

    public void setRequired_level(int required_level) {
        this.required_level = required_level;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<AchievementCertificateDto> getRequired_achievements_certificate() {
        return required_achievements_certificate;
    }

    public void setRequired_achievements_certificate(List<AchievementCertificateDto> required_certificates) {
        this.required_achievements_certificate = required_certificates;
    }

    public List<AchievementCategorizedDto> getRequired_achievements_categorized() {
        return required_achievements_categorized;
    }

    public void setRequired_achievements_categorized(List<AchievementCategorizedDto> required_achievements_categorized) {
        this.required_achievements_categorized = required_achievements_categorized;
    }

    public List<AchievementSpecialDto> getRequired_achievements_special() {
        return required_achievements_special;
    }

    public void setRequired_achievements_special(List<AchievementSpecialDto> required_achievements_special) {
        this.required_achievements_special = required_achievements_special;
    }

    public List<AchievementSpecialDto> getGain_achievements_special() {
        return gain_achievements_special;
    }

    public void setGain_achievements_special(List<AchievementSpecialDto> gain_achievements) {
        this.gain_achievements_special = gain_achievements;
    }

    public List<JobSessionDto> getSessions() {
        return sessions;
    }

    public void setSessions(List<JobSessionDto> sessions) {
        this.sessions = sessions;
    }

    public List<JobReviewDto> getJobReviewDtos() {
        return jobReviewDtos;
    }

    public void setJobReviewDtos(List<JobReviewDto> jobReviewDtos) {
        this.jobReviewDtos = jobReviewDtos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "OfferDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", short_name='" + short_name + '\'' +
                ", possible_xp_reward=" + possible_xp_reward +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", salary=" + salary +
                ", location='" + location + '\'' +
                ", required_level=" + required_level +
                ", author=" + authorId +
                ", required_achievements_certificate=" + required_achievements_certificate +
                ", required_achievements_categorized=" + required_achievements_categorized +
                ", required_achievements_special=" + required_achievements_special +
                ", gain_achievements_special=" + gain_achievements_special +
                ", sessionsDto=" + sessions +
                '}';
    }
}
