package com.cms.service;

import org.springframework.http.ResponseEntity;

import com.cms.responsedto.StudentResponse;
import com.cms.util.ResponseStructure;

public interface StudentService {

	ResponseEntity<ResponseStructure<StudentResponse>> getStudent();

}
