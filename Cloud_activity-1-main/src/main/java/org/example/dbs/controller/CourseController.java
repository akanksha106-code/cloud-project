package org.example.dbs.controller;

import org.example.dbs.model.Course;
import org.example.dbs.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("courses", service.getAllCourses());
        return "courses/list";
    }

    @GetMapping("/add")
    public String addCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "courses/add";
    }

    @PostMapping("/add")
    public String saveCourse(Course course) {
        service.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model) {
        model.addAttribute("course", service.getCourseById(id));
        return "courses/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id, Course course) {
        course.setId(id);
        service.saveCourse(course);
        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        service.deleteCourse(id);
        return "redirect:/courses";
    }
}
