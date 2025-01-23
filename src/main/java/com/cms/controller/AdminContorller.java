package com.cms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.requestdto.TeacherReuest;
import com.cms.requestdto.TeacherUpdateRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.responsedto.TeacherResponse;
import com.cms.service.AdminService;
import com.cms.util.ErrorStructure;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@AllArgsConstructor
@RequestMapping("/cms/admin")
public class AdminContorller {

	private static final Logger logger = LoggerFactory.getLogger(AdminContorller.class); // Logger instance
	private AdminService adminService;

	@Operation(description = "the End point is used to save the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PostMapping("/add-teacher")
	public ResponseEntity<ResponseStructure<TeacherResponse>> addTeacher(
			@Valid @RequestBody TeacherReuest teacherRequest) {
		logger.info("Fetching teacher with ID: {}", teacherRequest);
		ResponseEntity<ResponseStructure<TeacherResponse>> response = adminService.addTeacher(teacherRequest);
		logger.info("Fetched teacher data: {}", response);
		return response;
	}

	@Operation(description = "the End point is used to get the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teachre data Found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@GetMapping("/{id}/teachers")
	public ResponseEntity<ResponseStructure<TeacherResponse>> getTeacherById(@PathVariable("id") String id) {
		logger.info("Fetching teacher with ID: {}", id);
		ResponseEntity<ResponseStructure<TeacherResponse>> response = adminService.getTeacherById(id);
		logger.info("Fetched teacher data: {}", response);
		return response;
	}

	@Operation(description = "the End point is used to get ALl Teachres  data", responses = {
			@ApiResponse(responseCode = "200", description = "Teachers data Found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@GetMapping("teachers")
	public ResponseEntity<ResponseStructure<List<TeacherResponse>>> getAllTeachers() {

		logger.info("Fetching All Teachers : {}");
		ResponseEntity<ResponseStructure<List<TeacherResponse>>> allTeachers = adminService.getAllTeachers();
		logger.info("Fetched teacher data: {}", allTeachers);
		return allTeachers;

	}

	@Operation(description = "the End point is used to Update the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher Is Updated"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@PutMapping("/{id}/teachers")
	public ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher(
			@Valid @RequestBody TeacherUpdateRequest teacherReuest, @PathVariable("id") String id) {
		return adminService.updateTeacher(teacherReuest, id);
	}

	@Operation(description = "the End point is used to delete the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher is deleted"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@DeleteMapping("{id}/teachers")
	public ResponseEntity<SimpleResponseStructure> deleteTeacher(@PathVariable("id") String id) {

		return adminService.deleteTeacher(id);
	}

	@Operation(description = "the End point is used to save the Students data", responses = {
			@ApiResponse(responseCode = "200", description = "Student is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@PostMapping("/add-student")
	public ResponseEntity<ResponseStructure<StudentResponse>> addStudent(
			@Valid @RequestBody StudentRequest studentRequest) {
		return adminService.addStudent(studentRequest);
	}

	@Operation(description = "the End point is used to get the Student data", responses = {
			@ApiResponse(responseCode = "200", description = "Student data found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@GetMapping("/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(@PathVariable("sId") String sId) {
		return adminService.getStudent(sId);
	}

	@Operation(description = "the End point is used to save the Product data", responses = {
			@ApiResponse(responseCode = "200", description = "product is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@PutMapping("/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(
			@Valid @RequestBody StudentUpdateRequest stuUpdateRequest, @PathVariable("sId") String sId) {

		return adminService.updateStudent(stuUpdateRequest, sId);
	}

	@Operation(description = "the End point is used to save the Product data", responses = {
			@ApiResponse(responseCode = "200", description = "product is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@DeleteMapping("/{sId}/students")
	public ResponseEntity<SimpleResponseStructure> deleteStudent(@PathVariable("sId") String sId) {

		return adminService.deleteStudent(sId);
	}

	@Operation(description = "the End point is used to save the Product data", responses = {
			@ApiResponse(responseCode = "200", description = "product is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@GetMapping("/students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudents() {

		return adminService.getAllStudents();
	}

	@Operation(description = "the End point is used to save the Product data", responses = {
			@ApiResponse(responseCode = "200", description = "product is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@PostMapping("/{teacherId}/teachers/{sId}/students")
	public ResponseEntity<ResponseStructure<StudentResponse>> addTeacheToStudent(
			@PathVariable("teacherId") String teacherId, @PathVariable("sId") String sId) {

		return adminService.addTeacherToStudent(sId, teacherId);
	}

}
