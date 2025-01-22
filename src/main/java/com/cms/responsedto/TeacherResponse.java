package com.cms.responsedto;

import java.util.List;

import com.cms.entity.Student;
import com.cms.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@NoArgsConstructor
@Getter
@Setter
public class TeacherResponse {

	private String name;
	private String username;
	private Role role;
	private String subject;
	private List<Student> students;
}
