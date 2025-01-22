package com.cms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cms.entity.Student;

public interface StudentRepository extends MongoRepository<Student, String> {

	List<Student> findAllByTeacherId(String id);

	Optional<Student> findByUsername(String username);

	boolean existsByUsername(String string);

}
