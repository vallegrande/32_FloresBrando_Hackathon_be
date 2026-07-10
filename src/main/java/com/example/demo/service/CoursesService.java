package com.example.demo.service;

import com.example.demo.entity.Courses;
import com.example.demo.repository.CoursesRepository;
import com.example.demo.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursesService {

    private final CoursesRepository coursesRepository;

    public CoursesService(CoursesRepository coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    public List<Courses> findAll() {
        return coursesRepository.findAllActive();
    }

    public Optional<Courses> findById(Integer id) {
        return coursesRepository.findActiveById(id);
    }

    public Courses save(Courses course) {
        if (course.getStatus() == null) {
            course.setStatus(true);
        }
        return coursesRepository.save(course);
    }

    public Courses update(Integer id, Courses courseDetails) {
        Courses course = coursesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe courses con id: " + id));
        course.setName(courseDetails.getName());
        course.setCredits(courseDetails.getCredits());
        course.setTeacher(courseDetails.getTeacher());
        course.setCareer(courseDetails.getCareer());
        return coursesRepository.save(course);
    }

    public void delete(Integer id) {
        Courses course = coursesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe courses con id: " + id));
        course.setStatus(false);
        coursesRepository.save(course);
    }

    public void restore(Integer id) {
        Courses course = coursesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe courses con id: " + id));
        course.setStatus(true);
        coursesRepository.save(course);
    }

    public void changeStatus(Integer id, Boolean status) {
        Courses course = coursesRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe courses con id: " + id));
        course.setStatus(status);
        coursesRepository.save(course);
    }

    public List<Courses> findByCareer(Integer careerId) {
        return coursesRepository.findActiveByCareer(careerId);
    }
}
