package com.cms.responsedto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentResponse {

	private String sId;
	private String name;
	private String usernmae;
	private List<Integer> marks;
	private double grade;

}
