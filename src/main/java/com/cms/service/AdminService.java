package com.cms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.requestdto.TeacherReuest;
import com.cms.requestdto.TeacherUpdateRequest;
import com.cms.requestdto.UserRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.responsedto.TeacherResponse;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import jakarta.validation.Valid;

public interface AdminService {

	ResponseEntity<ResponseStructure<TeacherResponse>> addTeacher(TeacherReuest teacherRequest);

	ResponseEntity<ResponseStructure<TeacherResponse>> getTeacherById(String id);

	ResponseEntity<ResponseStructure<List<TeacherResponse>>> getAllTeachers();

	ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher(TeacherUpdateRequest teacherReuest, String id);

	ResponseEntity<SimpleResponseStructure> deleteTeacher(String id);

	ResponseEntity<ResponseStructure<StudentResponse>> addStudent(@Valid StudentRequest studentRequest);

	ResponseEntity<ResponseStructure<StudentResponse>> getStudent(String sId);

	ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(@Valid StudentUpdateRequest stuUpdateRequest,
			String sId);

	ResponseEntity<SimpleResponseStructure> deleteStudent(String sId);

	ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudents();

	ResponseEntity<ResponseStructure<StudentResponse>> addTeacherToStudent(String sId, String teacherId);

}
