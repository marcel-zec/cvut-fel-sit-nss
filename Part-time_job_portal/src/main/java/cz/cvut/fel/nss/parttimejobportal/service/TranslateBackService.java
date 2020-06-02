package cz.cvut.fel.nss.parttimejobportal.service;

import cz.cvut.fel.nss.parttimejobportal.dao.AddressDao;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
import cz.cvut.fel.nss.parttimejobportal.dto.AddressDto;
import cz.cvut.fel.nss.parttimejobportal.dto.UserDto;
import cz.cvut.fel.nss.parttimejobportal.exception.NotFoundException;
import cz.cvut.fel.nss.parttimejobportal.model.AbstractUser;
import cz.cvut.fel.nss.parttimejobportal.model.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class TranslateBackService {

    private final UserDao userDao;
    private final AddressDao addressDao;


    @Autowired
    public TranslateBackService(UserDao userDao, AddressDao addressDao) {
        this.userDao = userDao;
        this.addressDao = addressDao;
    }


    @Transactional
    public AbstractUser translateUser(UserDto userDto) throws NotFoundException {

        Objects.requireNonNull(userDto);
        AbstractUser user = userDao.find(userDto.getId());
        if (user == null) throw new NotFoundException();

        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setAddress(translateAddress(userDto.getAddress()));

        return user;
    }

    @Transactional
    public Address translateAddress(AddressDto addressDto) {
        Objects.requireNonNull(addressDto);
        Address address = addressDao.find(addressDto.getId());

        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setHouseNumber(addressDto.getHouseNumber());
        address.setStreet(addressDto.getStreet());
        address.setZipCode(addressDto.getZipCode());
        return address;
    }


}
