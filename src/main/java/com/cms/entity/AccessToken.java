package com.cms.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {
	@Id
	private String tokenId;
	private String token;
	private LocalDateTime expiration;
	private boolean isBlocked;
	private User user;
}
