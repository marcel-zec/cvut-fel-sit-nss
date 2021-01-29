package cz.cvut.fel.nss.parttimejobportal.dto;

import javax.persistence.Basic;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class CategoryDto implements Serializable
{

    @NotNull(message = "Id cannot be blank")
    private Long id;

    @Basic(optional = false)
    @NotNull(message = "Name of category cannot be blank")
    private String name;



    public CategoryDto(@NotNull(message = "Id cannot be blank") Long id,
                       @NotNull(message = "Name of category cannot be blank") String name) {

        this.id = id;
        this.name = name;

    }


    public CategoryDto() {

    }


    public String getName() {

        return name;
    }


    public void setName(String name) {

        this.name = name;
    }


    public Long getId() {

        return id;
    }


    public void setId(Long id) {

        this.id = id;
    }
}
