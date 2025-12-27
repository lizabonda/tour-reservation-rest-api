//package cz.cvut.fel.ear.projekt.service;
//
//import cz.cvut.fel.ear.projekt.dao.UserDao;
//import cz.cvut.fel.ear.projekt.model.User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Objects;
//
//@Service
//@Transactional
//public class UserService {
//    private final UserDao userDao;
//
//    public UserService(UserDao userDao) {
//        this.userDao = userDao;
//    }
//    public void createUser(User user) {
//        Objects.requireNonNull(user, "user is required");
//        user.validateAdult();
//        userDao.save(user);
//    }
//}
