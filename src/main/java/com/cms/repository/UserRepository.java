package com.cms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cms.entity.User;
import com.cms.enums.Role;

public interface UserRepository extends MongoRepository<User, String> {

	Optional<User> findByUsername(String username);

	boolean existsByUsername(String username);

	boolean existsByRole(Role role);
}
