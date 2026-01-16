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
        logger.info("CourseService.getAllCourses() - ENTRY");
        try {
            List<Course> courses = repo.findAll();
            logger.debug("CourseService.getAllCourses() - Found {} courses", courses.size());
            logger.info("CourseService.getAllCourses() - EXIT - Success - Returned {} courses", courses.size());
            return courses;
        } catch (Exception e) {
            logger.error("CourseService.getAllCourses() - EXIT - Error occurred while fetching courses", e);
            throw e;
        }
    }

    public Course getCourseById(Long id) {
        logger.info("CourseService.getCourseById() - ENTRY - courseId: {}", id);
        try {
            Course course = repo.findById(id).orElse(null);
            if (course == null) {
                logger.warn("CourseService.getCourseById() - Course not found with id: {}", id);
                logger.info("CourseService.getCourseById() - EXIT - Course not found");
            } else {
                logger.info("CourseService.getCourseById() - EXIT - Success - Found course: {}", course.getCourseCode());
            }
            return course;
        } catch (Exception e) {
            logger.error("CourseService.getCourseById() - EXIT - Error occurred while fetching course with id: {}", id, e);
            throw e;
        }
    }

    public Course saveCourse(Course course) {
        logger.info("CourseService.saveCourse() - ENTRY - courseCode: {}", course.getCourseCode());
        try {
            Course savedCourse = repo.save(course);
            logger.info("CourseService.saveCourse() - EXIT - Success - Course saved with id: {}", savedCourse.getId());
            return savedCourse;
        } catch (Exception e) {
            logger.error("CourseService.saveCourse() - EXIT - Error occurred while saving course: {}", course.getCourseCode(), e);
            throw e;
        }
    }

    public void deleteCourse(Long id) {
        logger.info("CourseService.deleteCourse() - ENTRY - courseId: {}", id);
        try {
            repo.deleteById(id);
            logger.info("CourseService.deleteCourse() - EXIT - Success - Course deleted with id: {}", id);
        } catch (Exception e) {
            logger.error("CourseService.deleteCourse() - EXIT - Error occurred while deleting course with id: {}", id, e);
            throw e;
        }
    }
}