package org.example.dbs.service;

import org.example.dbs.model.Course;
import org.example.dbs.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository repo;

    public CourseService(CourseRepository repo) {
        this.repo = repo;
    }

    public List<Course> getAllCourses() {
        logger.info("Fetching all courses");
        return repo.findAll();
    }

    public Course getCourseById(Long id) {
        logger.info("Fetching course with id: {}", id);
        return repo.findById(id).orElse(null);
    }

    public Course saveCourse(Course course) {
        logger.info("Saving course: {}", course.getCourseCode());
        return repo.save(course);
    }

    public void deleteCourse(Long id) {
        logger.info("Deleting course with id: {}", id);
        repo.deleteById(id);
    }
}
