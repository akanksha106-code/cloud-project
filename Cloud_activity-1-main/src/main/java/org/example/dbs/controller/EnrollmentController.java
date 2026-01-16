package org.example.dbs.controller;

import org.example.dbs.service.CourseService;
import org.example.dbs.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
    private final CourseService courseService;
    private final UserService userService;

    public EnrollmentController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public String index(Model model) {
        logger.info("EnrollmentController.index() - ENTRY");
        try {
            model.addAttribute("courses", courseService.getAllCourses());
            model.addAttribute("users", userService.getAllUsers());
            logger.info("EnrollmentController.index() - EXIT - Success");
            return "enrollments/list";
        } catch (Exception e) {
            logger.error("EnrollmentController.index() - EXIT - Error occurred", e);
            throw e;
        }
    }
}