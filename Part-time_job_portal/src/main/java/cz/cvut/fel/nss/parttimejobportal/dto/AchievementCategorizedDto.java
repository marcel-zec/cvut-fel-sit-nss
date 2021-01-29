package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import java.util.List;

public class AchievementCategorizedDto extends AchievementDto{
    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    private String name;

    @Basic(optional = false)
    private String description;

    @Basic(optional = false)
    private String icon;

    private List<Long> trips;
    private List<Long> owned_travel_journals;

    @Basic(optional = false)
    private int limit;

    @Basic(optional = false)
    private long categoryId;

    public AchievementCategorizedDto(@NotNull(message = "Id cannot be blank") Long id, String name, String description, String icon, List<Long> trips, List<Long> owned_travel_journals, int limit, long categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.trips = trips;
        this.owned_travel_journals = owned_travel_journals;
        this.limit = limit;
        this.categoryId = categoryId;
    }


    public AchievementCategorizedDto() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Long> getTrips() {
        return trips;
    }

    public void setTrips(List<Long> trips) {
        this.trips = trips;
    }

    public List<Long> getOwned_travel_journals() {
        return owned_travel_journals;
    }

    public void setOwned_travel_journals(List<Long> owned_travel_journals) {
        this.owned_travel_journals = owned_travel_journals;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
