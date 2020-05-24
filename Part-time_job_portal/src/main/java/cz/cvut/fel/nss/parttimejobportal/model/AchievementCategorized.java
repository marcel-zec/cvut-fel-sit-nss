package cz.cvut.fel.nss.parttimejobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="ACHIEVEMENT_CATEGORIZED")
public class AchievementCategorized extends Achievement {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Basic(optional = false)
    @Column(nullable = false, name = "limitOf")
    private int limit;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "achievement_categorized_owned_travel_journals",
            joinColumns = @JoinColumn(name = "achievement_categorized_id"),
            inverseJoinColumns = @JoinColumn(name = "traveljournal_id"))
    private List<TravelJournal> owned_travel_journals;

    public AchievementCategorized() {
        owned_travel_journals = new ArrayList<TravelJournal>();
    }

    public AchievementCategorized(String name, String description, String icon) {
        super(name, description, icon);
        owned_travel_journals = new ArrayList<TravelJournal>();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<TravelJournal> getOwned_travel_journals() {
        return owned_travel_journals;
    }

    public void setOwned_travel_journals(List<TravelJournal> owned_travel_journals) {
        this.owned_travel_journals = owned_travel_journals;
    }

    public void addTravelJournal(TravelJournal travelJournal) {
        this.owned_travel_journals.add(travelJournal);
    }
}
