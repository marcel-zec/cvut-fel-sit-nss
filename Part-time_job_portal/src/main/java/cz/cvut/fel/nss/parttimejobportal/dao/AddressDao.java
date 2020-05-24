package cz.cvut.fel.nss.parttimejobportal.dao;

import cz.cvut.fel.nss.parttimejobportal.model.Address;
import org.springframework.stereotype.Repository;

@Repository
public class AddressDao extends BaseDao<Address> {

    public AddressDao() {

        super(Address.class);
    }
}
