package org.example.dbs.controller;

import org.example.dbs.model.User;
import org.example.dbs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model) {
        logger.info("UserController.index() - ENTRY");
        try {
            model.addAttribute("users", service.getAllUsers());
            logger.info("UserController.index() - EXIT - Success");
            return "index";
        } catch (Exception e) {
            logger.error("UserController.index() - EXIT - Error occurred", e);
            throw e;
        }
    }

    @GetMapping("/add")
    public String addUserForm(Model model) {
        logger.info("UserController.addUserForm() - ENTRY");
        try {
            model.addAttribute("user", new User());
            logger.info("UserController.addUserForm() - EXIT - Success");
            return "add-user";
        } catch (Exception e) {
            logger.error("UserController.addUserForm() - EXIT - Error occurred", e);
            throw e;
        }
    }

    @PostMapping("/add")
    public String saveUser(User user) {
        logger.info("UserController.saveUser() - ENTRY - email: {}", user.getEmail());
        try {
            service.saveUser(user);
            logger.info("UserController.saveUser() - EXIT - Success - Redirecting to index");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("UserController.saveUser() - EXIT - Error occurred while saving user: {}", user.getEmail(), e);
            throw e;
        }
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        logger.info("UserController.editUser() - ENTRY - userId: {}", id);
        try {
            User user = service.getUserById(id);
            if (user == null) {
                logger.warn("UserController.editUser() - User not found with id: {}", id);
            }
            model.addAttribute("user", user);
            logger.info("UserController.editUser() - EXIT - Success");
            return "edit-user";
        } catch (Exception e) {
            logger.error("UserController.editUser() - EXIT - Error occurred for userId: {}", id, e);
            throw e;
        }
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id, User user) {
        logger.info("UserController.updateUser() - ENTRY - userId: {}, email: {}", id, user.getEmail());
        try {
            user.setId(id);
            service.saveUser(user);
            logger.info("UserController.updateUser() - EXIT - Success - Redirecting to index");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("UserController.updateUser() - EXIT - Error occurred for userId: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        logger.info("UserController.deleteUser() - ENTRY - userId: {}", id);
        try {
            service.deleteUser(id);
            logger.info("UserController.deleteUser() - EXIT - Success - Redirecting to index");
            return "redirect:/";
        } catch (Exception e) {
            logger.error("UserController.deleteUser() - EXIT - Error occurred for userId: {}", id, e);
            throw e;
        }
    }
}