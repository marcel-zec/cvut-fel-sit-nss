package cz.cvut.fel.nss.parttimejobportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "ACHIEVEMENT_CERTIFICATE")
public class AchievementCertificate extends Achievement{

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "achievement_certificate_owned_travel_journals",
            joinColumns = @JoinColumn(name = "achievement_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "traveljournal_id"))
    private List<TravelJournal> owned_travel_journals;

    public AchievementCertificate() {
    }

    public AchievementCertificate(String name, String description, String icon) {
        super(name, description, icon);
        owned_travel_journals = new ArrayList<>();
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
