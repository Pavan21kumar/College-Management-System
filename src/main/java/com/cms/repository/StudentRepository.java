package com.cms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cms.entity.Student;
import com.cms.entity.Teacher;
import com.cms.entity.User;
import com.cms.enums.Role;

public interface StudentRepository extends MongoRepository<Student, String> {

	List<Student> findAllByTeacherId(String id);

	Optional<Student> findByUsername(String username);

	boolean existsByUsername(String string);

	List<Student> findByRole(Role student);

	List<Student> findAllByTeacherIdAndRole(String id, Role teacher);

	Optional<Student> findByIdAndRole(String sId, Role student);

	Optional<Student> findByTeacherIdAndRole(String sId, Role student);

}
