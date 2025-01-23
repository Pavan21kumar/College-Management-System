package com.cms.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.cms.entity.User;
import com.cms.enums.Role;
import com.cms.exceptions.UnauthorizedException;
import com.cms.exceptions.UserIsNotLoginException;
import com.cms.repository.StudentRepository;
import com.cms.repository.UserRepository;
import com.cms.responsedto.StudentResponse;
import com.cms.service.StudentService;
import com.cms.util.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {
	private static final Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

	private StudentRepository studentRepo;
	private UserRepository userRepo;
	private TeacherServiceImpl teacherServiceImpl;
	private ResponseStructure<StudentResponse> studentResponseStructure;

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent() {
		logger.info("Attempting to get a Student (own details: {}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to Get Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.STUDENT.name())) {
			logger.error("Unauthorized user: {} attempted to get Student", username);
			throw new UnauthorizedException("Only Student and Teachers Can Access This Page");
		}
		return studentRepo.findByUsername(username).map(student -> {
			StudentResponse studeResponse = teacherServiceImpl.mapToStudentResponse(student);
			logger.debug("Student data found :", student);
			return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("data Found").setBody(studeResponse));
		}).get();
	}

}
