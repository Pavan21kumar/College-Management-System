package com.cms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cms.entity.Teacher;
import com.cms.enums.Role;

public interface TeacherRepository extends MongoRepository<Teacher, String> {

	Optional<Teacher> findByUsername(String username);

	List<Teacher> findAllByRole(Role teacher);

}
