package cz.cvut.fel.nss.parttimejobportal.service.security;

import cz.cvut.fel.nss.parttimejobportal.model.Role;
import cz.cvut.fel.nss.parttimejobportal.model.User;
import cz.cvut.fel.nss.parttimejobportal.dao.UserDao;
import cz.cvut.fel.nss.parttimejobportal.exception.BadPassword;
import cz.cvut.fel.nss.parttimejobportal.exception.NotAllowedException;
import cz.cvut.fel.nss.parttimejobportal.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessService {
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private UserDao userDao;

    @Autowired
    public AccessService(PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, UserDao userDao) {

        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.userDao = userDao;
    }

    @Transactional
    public void adminAccess(Long admin_id) throws NotAllowedException {

        final User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser.getRole().equals(Role.ADMIN)) {
            if (!currentUser.getId().equals(admin_id)) throw new NotAllowedException("Access denied.");
        }
    }

    @Transactional
    public void userAccess(Long user_id) throws NotAllowedException {

        final User currentUser = SecurityUtils.getCurrentUser();
        if (currentUser.getRole().equals(Role.USER)) {
            if (!currentUser.getId().equals(user_id)) throw new NotAllowedException("Access denied.");
        }
    }

    @Transactional
    public User getUser(User currentUser){
        return userDao.find(currentUser.getId());
    }


    @Transactional
    public void changePassword(String oldPassword, String newPassword, String newPasswordAgain) throws BadPassword {

        final User currentUser = SecurityUtils.getCurrentUser();
        if (!passwordEncoder.matches(oldPassword, userDao.find(currentUser.getId()).getPassword())) {
            throw new BadPassword();
        }else {
            if (newPassword.equals(newPasswordAgain)) {
                currentUser.setPassword(new BCryptPasswordEncoder().encode(newPassword));
                SimpleMailMessage msg = new SimpleMailMessage();
                msg.setTo(currentUser.getEmail());
                msg.setSubject("Password change");
                msg.setText("Hello your password has been changed.\n" + "If you did not change it, contact us as soon as possible." +
                        "\n \n With love IT team.");
                javaMailSender.send(msg);
                userDao.update(currentUser);
            }
        }
    }
}
