package com.cms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import com.cms.util.ErrorStructure;
import com.cms.util.ResponseStructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/cms/teacher")
public class TeacherController {

	private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

	private final TeacherService teacherService;

	@Operation(summary = "Add Student Data", description = "This endpoint is used to add student data for the current logged-in teacher.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Students data added successfully.", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('TEACHER')")
	@PostMapping("/add-students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> addStudent(
			@Valid @RequestBody List<@Valid StudentRequest> studentRequests) {
		logger.info("Received request to add student data: {}", studentRequests);
		return teacherService.addStudent(studentRequests);
	}

	@Operation(summary = "Get Student Data", description = "This endpoint retrieves the data of a specific student for the current logged-in teacher.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student data retrieved successfully.", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid student ID.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('TEACHER')")
	@GetMapping("/students/{sId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(@PathVariable("sId") String sId) {
		logger.info("Received request to retrieve student data for ID: {}", sId);
		return teacherService.getStudent(sId);
	}

	@Operation(summary = "Update Student Data", description = "This endpoint updates the data of a specific student for the current logged-in teacher.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student data updated successfully.", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request data.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('TEACHER')")
	@PutMapping("/students/{sId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(
			@Valid @RequestBody StudentUpdateRequest request, @PathVariable("sId") String sId) {
		logger.info("Received request to update student data for ID: {}. Request: {}", sId, request);
		return teacherService.updateStudents(request, sId);
	}

	@Operation(summary = "Get All Students", description = "This endpoint retrieves the data of all students associated with the current logged-in teacher.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All student data retrieved successfully.", content = @Content(schema = @Schema(implementation = ResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('TEACHER')")
	@GetMapping("/students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudentsByTeacher() {
		logger.info("Received request to retrieve all students for the logged-in teacher.");
		return teacherService.getALlStudentsByteacher();
	}
}