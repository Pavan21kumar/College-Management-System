package com.cms.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.util.ResponseStructure;

public interface TeacherService {

	ResponseEntity<ResponseStructure<List<StudentResponse>>> addStudent(List<StudentRequest> studentRequest);

	ResponseEntity<ResponseStructure<StudentResponse>> getStudent(String sId);

	ResponseEntity<ResponseStructure<StudentResponse>> updateStudents(StudentUpdateRequest request, String sId);

	ResponseEntity<ResponseStructure<List<StudentResponse>>> getALlStudentsByteacher();

}
