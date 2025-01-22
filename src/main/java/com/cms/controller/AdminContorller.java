package com.cms.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PutExchange;

import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.requestdto.TeacherReuest;
import com.cms.requestdto.TeacherUpdateRequest;
import com.cms.requestdto.UserRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.responsedto.TeacherResponse;
import com.cms.service.AdminService;
import com.cms.util.ErrorStructure;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.val;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@AllArgsConstructor
@RequestMapping("/cms/admin")
public class AdminContorller {

	private AdminService adminService;

	@Operation(description = "the End point is used to save the Product data", responses = {
			@ApiResponse(responseCode = "200", description = "product is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PostMapping("/add-teacher")
	public ResponseEntity<ResponseStructure<TeacherResponse>> addTeacher(
			@Valid @RequestBody TeacherReuest teacherRequest) {

		return adminService.addTeacher(teacherRequest);
	}

	@GetMapping("/{id}/teachers")
	public ResponseEntity<ResponseStructure<TeacherResponse>> getTeacherById(@PathVariable("id") String id) {
		return adminService.getTeacherById(id);
	}

	@GetMapping("teachers")
	public ResponseEntity<ResponseStructure<List<TeacherResponse>>> getAllTeachers() {
		return adminService.getAllTeachers();
	}

	@PutMapping("/{id}/teachers")
	public ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher(
			@Valid @RequestBody TeacherUpdateRequest teacherReuest, @PathVariable("id") String id) {
		return adminService.updateTeacher(teacherReuest, id);
	}

	@DeleteMapping("{id}/teachers")
	public ResponseEntity<SimpleResponseStructure> deleteTeacher(@PathVariable("id") String id) {

		return adminService.deleteTeacher(id);
	}

	@PostMapping("/add-student")
	public ResponseEntity<ResponseStructure<StudentResponse>> addStudent(
			@Valid @RequestBody StudentRequest studentRequest) {
		return adminService.addStudent(studentRequest);
	}

	@GetMapping("/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(@PathVariable("sId") String sId) {
		return adminService.getStudent(sId);
	}

	@PutMapping("/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(
			@Valid @RequestBody StudentUpdateRequest stuUpdateRequest, @PathVariable("sId") String sId) {

		return adminService.updateStudent(stuUpdateRequest, sId);
	}

	@DeleteMapping("/{sId}/students")
	public ResponseEntity<SimpleResponseStructure> deleteStudent(@PathVariable("sId") String sId) {

		return adminService.deleteStudent(sId);
	}

	@GetMapping("/students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudents() {

		return adminService.getAllStudents();
	}

	@PostMapping("/{teacherId}/teachers/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> addTeacheToStudent(
			@PathVariable("teacherId") String teacherId, @PathVariable("sId") String sId) {

		return adminService.addTeacherToStudent(sId,teacherId);
	}

}
