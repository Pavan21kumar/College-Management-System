package com.cms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.service.TeacherService;
import com.cms.util.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/cms/teacher")
public class TeacherController {

	private TeacherService teacherService;

	@PostMapping("/add-students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> addStudent(
			@Valid @RequestBody List<StudentRequest> studentRequest) {
		return teacherService.addStudent(studentRequest);

	}

	@GetMapping("/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(@PathVariable("sId") String sId) {
		return teacherService.getStudent(sId);
	}

	@PutMapping("/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@Valid @RequestBody StudentUpdateRequest request,
			@PathVariable("sId") String sId) {
		return teacherService.updateStudents(request, sId);
	}

	@GetMapping("/students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudentsByTeacher() {

		return teacherService.getALlStudentsByteacher();
	}
}
