package org.example.dbs.controller;

import org.example.dbs.model.Course;
import org.example.dbs.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public String index(Model model) {
        logger.info("CourseController.index() - ENTRY");
        try {
            model.addAttribute("courses", service.getAllCourses());
            logger.info("CourseController.index() - EXIT - Success");
            return "courses/list";
        } catch (Exception e) {
            logger.error("CourseController.index() - EXIT - Error occurred", e);
            throw e;
        }
    }

    @GetMapping("/add")
    public String addCourseForm(Model model) {
        logger.info("CourseController.addCourseForm() - ENTRY");
        try {
            model.addAttribute("course", new Course());
            logger.info("CourseController.addCourseForm() - EXIT - Success");
            return "courses/add";
        } catch (Exception e) {
            logger.error("CourseController.addCourseForm() - EXIT - Error occurred", e);
            throw e;
        }
    }

    @PostMapping("/add")
    public String saveCourse(Course course) {
        logger.info("CourseController.saveCourse() - ENTRY - courseCode: {}", course.getCourseCode());
        try {
            service.saveCourse(course);
            logger.info("CourseController.saveCourse() - EXIT - Success - Redirecting to courses");
            return "redirect:/courses";
        } catch (Exception e) {
            logger.error("CourseController.saveCourse() - EXIT - Error occurred while saving course: {}", course.getCourseCode(), e);
            throw e;
        }
    }

    @GetMapping("/edit/{id}")
    public String editCourse(@PathVariable Long id, Model model) {
        logger.info("CourseController.editCourse() - ENTRY - courseId: {}", id);
        try {
            Course course = service.getCourseById(id);
            if (course == null) {
                logger.warn("CourseController.editCourse() - Course not found with id: {}", id);
            }
            model.addAttribute("course", course);
            logger.info("CourseController.editCourse() - EXIT - Success");
            return "courses/edit";
        } catch (Exception e) {
            logger.error("CourseController.editCourse() - EXIT - Error occurred for courseId: {}", id, e);
            throw e;
        }
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Long id, Course course) {
        logger.info("CourseController.updateCourse() - ENTRY - courseId: {}, courseCode: {}", id, course.getCourseCode());
        try {
            course.setId(id);
            service.saveCourse(course);
            logger.info("CourseController.updateCourse() - EXIT - Success - Redirecting to courses");
            return "redirect:/courses";
        } catch (Exception e) {
            logger.error("CourseController.updateCourse() - EXIT - Error occurred for courseId: {}", id, e);
            throw e;
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        logger.info("CourseController.deleteCourse() - ENTRY - courseId: {}", id);
        try {
            service.deleteCourse(id);
            logger.info("CourseController.deleteCourse() - EXIT - Success - Redirecting to courses");
            return "redirect:/courses";
        } catch (Exception e) {
            logger.error("CourseController.deleteCourse() - EXIT - Error occurred for courseId: {}", id, e);
            throw e;
        }
    }
}