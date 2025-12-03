package org.example.dbs.service;

import org.example.dbs.model.User;
import org.example.dbs.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public List<User> getAllUsers() {
        logger.info("Fetching all users");
        List<User> users = repo.findAll();
        logger.debug("Found {} users", users.size());
        return users;
    }

    public User getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        User user = repo.findById(id).orElse(null);
        if (user == null) {
            logger.warn("User not found with id: {}", id);
        }
        return user;
    }

    public User saveUser(User user) {
        logger.info("Saving user: {}", user.getEmail());
        try {
            User savedUser = repo.save(user);
            logger.info("User saved successfully with id: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            logger.error("Error saving user: {}", user.getEmail(), e);
            throw e;
        }
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        try {
            repo.deleteById(id);
            logger.info("User deleted successfully with id: {}", id);
        } catch (Exception e) {
            logger.error("Error deleting user with id: {}", id, e);
            throw e;
        }
    }
}
