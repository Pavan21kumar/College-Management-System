package com.cms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.service.StudentService;
import com.cms.util.ResponseStructure;

import lombok.AllArgsConstructor;
import com.cms.responsedto.*;

@RestController
@AllArgsConstructor
@RequestMapping("/cms/students")
public class StudentController {

	private StudentService studentService;

	@GetMapping("/my-details")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent() {

		return studentService.getStudent();
	}
}
