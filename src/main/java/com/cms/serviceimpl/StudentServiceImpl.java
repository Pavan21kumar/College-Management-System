package com.cms.serviceimpl;

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

	private StudentRepository studentRepo;
	private UserRepository userRepo;
	private TeacherServiceImpl teacherServiceImpl;
	private ResponseStructure<StudentResponse> studentResponseStructure;

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.STUDENT.name()))
			throw new UnauthorizedException("Only Student and Teachers Can Access This Page");
		return studentRepo.findByUsername(username).map(student -> {
			StudentResponse studeResponse = teacherServiceImpl.mapToStudentResponse(student);
			return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("data Found").setBody(studeResponse));
		}).get();
	}

}
