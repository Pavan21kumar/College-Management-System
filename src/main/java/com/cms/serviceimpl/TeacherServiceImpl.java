package com.cms.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.entity.Student;
import com.cms.entity.Teacher;
import com.cms.entity.User;
import com.cms.enums.Role;
import com.cms.exceptions.NoStudentsFoundException;
import com.cms.exceptions.StudentNotFoundByIdException;
import com.cms.exceptions.StudentUsernameAllReadyPresentException;
import com.cms.exceptions.UnauthorizedException;
import com.cms.exceptions.UserIsNotLoginException;
import com.cms.repository.StudentRepository;
import com.cms.repository.TeacherRepository;
import com.cms.repository.UserRepository;
import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.service.TeacherService;
import com.cms.util.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {
	private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);
	private StudentRepository studentRepo;
	private TeacherRepository teacherRepo;
	private PasswordEncoder encoder;
	private ResponseStructure<List<StudentResponse>> studentsResponses;
	private ResponseStructure<StudentResponse> studnetResponseStructure;
	private UserRepository userRepo;

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> addStudent(List<StudentRequest> studentRequest) {
		logger.info("Attempting to Add a Student : {}", studentRequest);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to add Student .");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		Teacher teacher = teacherRepo.findByUsername(username).get();

		List<Student> students = mapToStudents(studentRequest, teacher.getId());

		studentRepo.saveAll(students);
		logger.debug("Students data saved :", students);
		return ResponseEntity.ok(studentsResponses.setStatusCode(HttpStatus.OK.value()).setMessage("Students are Added")
				.setBody(mapToStudentResponses(students)));

	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(String sId) {
		logger.info("Attempting to get a Student with Id: {}", sId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to get Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		return studentRepo.findById(sId).map(student -> {
			StudentResponse response = mapToStudentResponse(student);
			logger.debug("Student data found :", student);
			return ResponseEntity.ok(studnetResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("data Found").setBody(response));
		}).orElseThrow(() -> new StudentNotFoundByIdException("Student Not Found By Giove Id Or Invalid Id"));
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudents(StudentUpdateRequest request, String sId) {
		logger.info("Attempting to update a Student with Id: {}", sId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to update Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.TEACHER.name())) {
			logger.error("Unauthorized access attempt to update Student.");
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}

		return studentRepo.findById(sId).map(student -> {

			student = mapToStudent(student, request);
			studentRepo.save(student);
			logger.debug("Student data updated :", student);
			return ResponseEntity.ok(studnetResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("Data found ").setBody(mapToStudentResponses(student)));
		}).orElseThrow(() -> new StudentNotFoundByIdException("stundet Not Found By Given Id or Invalid Id"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getALlStudentsByteacher() {
		logger.info("Attempting to get all Student : {}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to getALl Students.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.TEACHER.name())) {
			logger.error("Unauthorized user: {} attempted to get All Students", username);
			throw new UnauthorizedException("Only  and Teachers Can Access This Page");
		}
		return teacherRepo.findByUsername(username).map(teacher -> {
			List<Student> students = studentRepo.findAllByTeacherId(teacher.getId());
			if (students.isEmpty()) {
				logger.error("No Students Found");
				throw new NoStudentsFoundException("no Students Found");
			}
			return ResponseEntity.ok(studentsResponses.setStatusCode(HttpStatus.OK.value()).setMessage("data Found")
					.setBody(mapToStudentResponses(students)));
		}).get();

	}

	// *************** Helper Methods ***************
	public List<StudentResponse> mapToStudentResponses(List<Student> students) {
		List<StudentResponse> studentResponses = new ArrayList<>();
		for (Student s : students) {
			studentResponses.add(mapToStudentResponses(s));
		}
		return studentResponses;
	}

	public StudentResponse mapToStudentResponses(Student s) {

		return StudentResponse.builder().name(s.getName()).usernmae(s.getUsername()).marks(s.getMarks())
				.grade(s.getGrade()).build();
	}

	private List<Student> mapToStudents(List<StudentRequest> studentRequests, String id) {
		List<Student> students = new ArrayList<>();

		for (StudentRequest studentRequest : studentRequests) {
			students.add(mapToStudet(studentRequest, new Student(), id));
		}
		return students;
	}

	private Student mapToStudet(StudentRequest studentRequest, Student student, String id) {
		// TODO Auto-generated method stub
		student.setName(studentRequest.getName());
		student.setUsername(studentRequest.getEmail().split("@")[0]);
		student.setPassword(encoder.encode(studentRequest.getPassword()));
		student.setRole(Role.STUDENT);
		student.setMarks(Arrays.asList(studentRequest.getMarks()));
		student.setTeacherId(Arrays.asList(id));

		if (studentRepo.existsByUsername(student.getUsername()))
			throw new StudentUsernameAllReadyPresentException(
					"Username is Allready Presnet : " + student.getUsername());
		return student;
	}

	private Student mapToStudent(Student student, StudentUpdateRequest request) {

		student.setId(student.getId());
		if (request.getName() != null)
			student.setName(request.getName());
		if (request.getPassword() != null)
			student.setPassword(request.getPassword());
		if (request.getEmail() != null)
			student.setUsername(request.getEmail());
		if (request.getMarks() != 0)
			student.setMarks(Arrays.asList(request.getMarks()));
		if (request.getGrade() != 0)
			student.setGrade(request.getGrade());
		return student;
	}

	public StudentResponse mapToStudentResponse(Student student) {

		return StudentResponse.builder().name(student.getName()).usernmae(student.getUsername())
				.marks(student.getMarks()).grade(student.getGrade()).build();
	}

}
