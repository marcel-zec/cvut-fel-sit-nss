package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Admin;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDao extends BaseDao<Admin>{

    public AdminDao() {
        super(Admin.class);
    }
}
