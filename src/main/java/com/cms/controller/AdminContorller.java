package com.cms.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import jakarta.validation.Path.ReturnValueNode;
import lombok.AllArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@AllArgsConstructor
@RequestMapping("/cms")
public class AdminContorller {

	private static final Logger logger = LoggerFactory.getLogger(AdminContorller.class); // Logger instance
	private AdminService adminService;

	@Operation(description = "the End point is used to save the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/admin/add-teacher")
	public ResponseEntity<ResponseStructure<TeacherResponse>> addTeacher(
			@Valid @RequestBody TeacherReuest teacherRequest) {
		logger.info("Fetching teacher with ID: {}", teacherRequest);
		ResponseEntity<ResponseStructure<TeacherResponse>> response = adminService.addTeacher(teacherRequest);
		logger.info("Fetched teacher data: {}", response);
		return response;
	}

	@Operation(description = "the End point is used to get the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teachre data Found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/teachers/{id}")
	public ResponseEntity<ResponseStructure<TeacherResponse>> getTeacherById(@PathVariable("id") String id) {
		logger.info("Fetching teacher with ID: {}", id);
		ResponseEntity<ResponseStructure<TeacherResponse>> response = adminService.getTeacherById(id);
		logger.info("Fetched teacher data: {}", response);
		return response;
	}

	@Operation(description = "the End point is used to get ALl Teachres  data", responses = {
			@ApiResponse(responseCode = "200", description = "Teachers data Found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/teachers")
	public ResponseEntity<ResponseStructure<List<TeacherResponse>>> getAllTeachers() {

		logger.info("Fetching All Teachers : {}");
		ResponseEntity<ResponseStructure<List<TeacherResponse>>> allTeachers = adminService.getAllTeachers();
		logger.info("Fetched teacher data: {}", allTeachers);
		return allTeachers;

	}

	@Operation(description = "the End point is used to Update the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher Is Updated"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/admin/teachers/{id}")
	public ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher(
			@Valid @RequestBody TeacherUpdateRequest teacherReuest, @PathVariable("id") String id) {
		System.out.println("enter the method");
		logger.info("Updating Teacher Data  with ID: {}", id);
		ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher = adminService.updateTeacher(teacherReuest,
				id);
		logger.info("Fetching Updated teacher with ID: {}", updateTeacher);
		return updateTeacher;

	}

	@Operation(description = "the End point is used to delete the Teacher data", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher is deleted"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/admin/teachers/{id}")
	public ResponseEntity<SimpleResponseStructure> deleteTeacher(@PathVariable("id") String id) {

		logger.info("Delete teacher with ID: {}", id);
		ResponseEntity<SimpleResponseStructure> deleteTeacher = adminService.deleteTeacher(id);
		logger.info("Delete teacher with ID: {}", id);
		return deleteTeacher;

	}

	@Operation(description = "the End point is used to save the Students data", responses = {
			@ApiResponse(responseCode = "200", description = "Student is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/add-student")
	public ResponseEntity<ResponseStructure<StudentResponse>> addStudent(
			@Valid @RequestBody StudentRequest studentRequest) {
		logger.info("Addong  Student Data : {}", studentRequest);
		ResponseEntity<ResponseStructure<StudentResponse>> addStudent = adminService.addStudent(studentRequest);
		logger.info("Fetching StudentResponse : {}", addStudent);
		return addStudent;

	}

	@Operation(description = "the End point is used to get the Student data", responses = {
			@ApiResponse(responseCode = "200", description = "Student data found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/students/{sId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(@PathVariable("sId") String sId) {
		logger.info("Fetching Student with ID: {}", sId);
		ResponseEntity<ResponseStructure<StudentResponse>> student = adminService.getStudent(sId);
		logger.info("Fetching StudentResponse with ID: {}", student);
		return student;

	}

	@Operation(description = "the End point is used to save the Product data", responses = {
			@ApiResponse(responseCode = "200", description = "product is saved"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@PutMapping("/admin/students/{sId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(
			@Valid @RequestBody StudentUpdateRequest stuUpdateRequest, @PathVariable("sId") String sId) {
		logger.info("Updating Student  Data with ID: {}", sId);
		ResponseEntity<ResponseStructure<StudentResponse>> updateStudent = adminService.updateStudent(stuUpdateRequest,
				sId);
		logger.info("Fetching Updated Student Data  : {}", updateStudent);
		return updateStudent;
	}

	@Operation(description = "the End point is used to Delete the Student data", responses = {
			@ApiResponse(responseCode = "200", description = "Student data  is deleted"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@DeleteMapping("/admin/students/{sId}")
	public ResponseEntity<SimpleResponseStructure> deleteStudent(@PathVariable("sId") String sId) {
		logger.info("Delete Studentwith ID: {}", sId);
		ResponseEntity<SimpleResponseStructure> deleteStudent = adminService.deleteStudent(sId);
		logger.info("Delete student with ID: {}", deleteStudent);
		return deleteStudent;
	}

	@Operation(description = "the End point is used to Get ALl Students ", responses = {
			@ApiResponse(responseCode = "200", description = "Students data Found"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })

	@PreAuthorize("hasAuthority('ADMIN')")
	@GetMapping("/admin/students")
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudents() {
		logger.info("Fetching All Students : {}");
		ResponseEntity<ResponseStructure<List<StudentResponse>>> allStudents = adminService.getAllStudents();
		logger.info("Fetching ALl Students Responses : {}", allStudents);
		return allStudents;
	}

	@Operation(description = "the End point is used to Add Teacher To Student ", responses = {
			@ApiResponse(responseCode = "200", description = "Teacher  is added To Student"),
			@ApiResponse(responseCode = "400", description = "invaild inputs", content = @Content(schema = @Schema(implementation = ErrorStructure.class))),
			@ApiResponse(responseCode = "403", description = "Access denied or forbidden.", content = @Content(schema = @Schema(implementation = ErrorStructure.class))) })
	@PreAuthorize("hasAuthority('ADMIN')")
	@PostMapping("/admin/teachers/{teacherId}/students/{sId}")
	public ResponseEntity<ResponseStructure<StudentResponse>> addTeacheToStudent(
			@PathVariable("teacherId") String teacherId, @PathVariable("sId") String sId) {
		logger.info("Add  teacher to Student with ID: {}", teacherId, sId);
		ResponseEntity<ResponseStructure<StudentResponse>> addTeacherToStudent = adminService.addTeacherToStudent(sId,
				teacherId);
		logger.info("Fetching Student Response  : {}", addTeacherToStudent);
		return addTeacherToStudent;
	}

}
