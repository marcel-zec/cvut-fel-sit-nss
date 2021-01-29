package cz.cvut.fel.nss.parttimejobportal.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AchievementDto implements Serializable {
    public AchievementDto() {

    }
}
