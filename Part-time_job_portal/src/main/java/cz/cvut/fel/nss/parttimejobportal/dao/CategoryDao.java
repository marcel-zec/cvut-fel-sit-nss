package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Category;
import cz.cvut.fel.nss.parttimejobportal.model.Trip;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends BaseDao<Category>{
    protected CategoryDao() {
        super(Category.class);
    }

    public boolean add(Category category, Trip trip){
        return category.add(trip);
    }
}