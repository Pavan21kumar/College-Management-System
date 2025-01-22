package com.cms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cms.entity.RefreshToken;
import com.cms.entity.User;

public interface RefreshTokenRepo extends MongoRepository<RefreshToken, String> {

	boolean existsByTokenAndIsBlocked(String rt, boolean b);

	Optional<RefreshToken> findByToken(String refreshToken);

}
