package com.jeeproject.service;

import com.jeeproject.dao.UserDAO;
import com.jeeproject.model.User;

import java.util.List;

public class UserService {

    private static final UserDAO userDAO = new UserDAO();

    public static void addUser(User user) {
        userDAO.save(user);
    }

    public static User getUserById(int id) {
        return userDAO.findById(id);
    }

    public static User getUserByEmail(String email) { return userDAO.findByEmail(email); }

    public static List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public static void updateUser(User user) {
        userDAO.update(user);
    }

    public static void deleteUser(int id) {
        userDAO.delete(id);
    }

    public static User authenticate(String email, String password) {
        User user = getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
