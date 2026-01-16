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
        logger.info("UserService.getAllUsers() - ENTRY");
        try {
            List<User> users = repo.findAll();
            logger.debug("UserService.getAllUsers() - Found {} users", users.size());
            logger.info("UserService.getAllUsers() - EXIT - Success - Returned {} users", users.size());
            return users;
        } catch (Exception e) {
            logger.error("UserService.getAllUsers() - EXIT - Error occurred while fetching users", e);
            throw e;
        }
    }

    public User getUserById(Long id) {
        logger.info("UserService.getUserById() - ENTRY - userId: {}", id);
        try {
            User user = repo.findById(id).orElse(null);
            if (user == null) {
                logger.warn("UserService.getUserById() - User not found with id: {}", id);
                logger.info("UserService.getUserById() - EXIT - User not found");
            } else {
                logger.info("UserService.getUserById() - EXIT - Success - Found user: {}", user.getEmail());
            }
            return user;
        } catch (Exception e) {
            logger.error("UserService.getUserById() - EXIT - Error occurred while fetching user with id: {}", id, e);
            throw e;
        }
    }

    public User saveUser(User user) {
        logger.info("UserService.saveUser() - ENTRY - email: {}", user.getEmail());
        try {
            User savedUser = repo.save(user);
            logger.info("UserService.saveUser() - EXIT - Success - User saved with id: {}", savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            logger.error("UserService.saveUser() - EXIT - Error occurred while saving user: {}", user.getEmail(), e);
            throw e;
        }
    }

    public void deleteUser(Long id) {
        logger.info("UserService.deleteUser() - ENTRY - userId: {}", id);
        try {
            repo.deleteById(id);
            logger.info("UserService.deleteUser() - EXIT - Success - User deleted with id: {}", id);
        } catch (Exception e) {
            logger.error("UserService.deleteUser() - EXIT - Error occurred while deleting user with id: {}", id, e);
            throw e;
        }
    }
}