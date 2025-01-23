package com.cms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.service.StudentService;
import com.cms.serviceimpl.StudentServiceImpl;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import com.cms.responsedto.*;

@RestController
@AllArgsConstructor
@RequestMapping("/cms/students")
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class); // Logger instance
	private StudentService studentService;

	@Operation(summary = "Get Student Data", description = "This endpoint is used to Get a Student data .current Login Student  details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student Is found ", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))),
			@ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))),
			@ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = SimpleResponseStructure.class))) })
	@GetMapping("/my-details")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent() {

		logger.info("Fetching StudentDetails : {}");
		ResponseEntity<ResponseStructure<StudentResponse>> student = studentService.getStudent();
		logger.info("Fetching StudentResponse : {}", student);
		return student;

	}
}
