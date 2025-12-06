package org.example.dbs.controller;

import org.example.dbs.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final CourseService courseService;

    public EnrollmentController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "courses/list";
    }

}
