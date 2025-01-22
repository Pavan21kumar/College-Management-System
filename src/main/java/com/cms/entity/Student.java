package com.cms.entity;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
public class Student extends User {

	private double grade;
	private List<Integer> marks;
	private List<String> teacherId;

}
