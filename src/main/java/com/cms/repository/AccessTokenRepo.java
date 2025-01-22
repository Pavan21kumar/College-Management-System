package com.cms.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cms.entity.AccessToken;
import com.cms.entity.User;

public interface AccessTokenRepo extends MongoRepository<AccessToken, String> {

	boolean existsByTokenAndIsBlocked(String at, boolean b);

	Optional<AccessToken> findByToken(String accessToken);

}
