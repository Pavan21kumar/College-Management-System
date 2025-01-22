package com.cms.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import com.cms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "users")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
//@Inheritance(strategy = InheritanceType.JOINED)
public class User {

	private String id;
	private String name;
	private String username;
	private String password;
	private Role role;
}
