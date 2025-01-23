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
import com.cms.exceptions.EmailAllReadyPresentexception;
import com.cms.exceptions.NoStudentsFoundException;
import com.cms.exceptions.NoTeachersFoundException;
import com.cms.exceptions.StudentNotFoundByIdException;
import com.cms.exceptions.StudentUsernameAllReadyPresentException;
import com.cms.exceptions.TeacherIdNotFoundException;
import com.cms.exceptions.UnauthorizedException;
import com.cms.exceptions.UserIsNotLoginException;
import com.cms.repository.StudentRepository;
import com.cms.repository.TeacherRepository;
import com.cms.repository.UserRepository;
import com.cms.requestdto.StudentRequest;
import com.cms.requestdto.StudentUpdateRequest;
import com.cms.requestdto.TeacherReuest;
import com.cms.requestdto.TeacherUpdateRequest;
import com.cms.responsedto.StudentResponse;
import com.cms.responsedto.TeacherResponse;
import com.cms.service.AdminService;
import com.cms.util.ResponseStructure;
import com.cms.util.SimpleResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
	private static final Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

	private UserRepository userRepo;
	private TeacherRepository teacherRepo;
	private ResponseStructure<TeacherResponse> teachersResponse;
	private ResponseStructure<List<TeacherResponse>> teachers;
	private StudentRepository studentRepo;
	private SimpleResponseStructure simplStructure;
	private PasswordEncoder encoder;
	private TeacherServiceImpl teacherServiceImpl;
	private ResponseStructure<StudentResponse> studentResponseStructure;
	private ResponseStructure<List<StudentResponse>> studentResponses;

	@Override
	public ResponseEntity<ResponseStructure<TeacherResponse>> addTeacher(TeacherReuest teacherRequest) {
		logger.info("Attempting to add a teacher: {}", teacherRequest);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to add teacher.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		return userRepo.findByUsername(username).map(user -> {
			if (!user.getRole().name().equals(Role.ADMIN.name()))
				throw new UnauthorizedException("Only Admin Can Access This Page");

			if (teacherRepo.findByUsername(teacherRequest.getEmail().split("@")[0]).isPresent()) {
				logger.debug("Authenticated user: {}", username);
				throw new EmailAllReadyPresentexception("email allready Present");
			}
			Teacher teacher = mapToTeacher(teacherRequest, new Teacher());

			teacher.setSubject(teacherRequest.getSubject());
			teacher = teacherRepo.save(teacher);
			logger.debug("Teacher data Saved: {}", teacher);
			return ResponseEntity.ok(teachersResponse.setStatusCode(HttpStatus.CREATED.value())
					.setMessage("Teacher is Added").setBody(maptoTeacherResponse(teacher)));

		}).orElseThrow(() -> new UnauthorizedException("unauthorized page"));

	}

	@Override
	public ResponseEntity<ResponseStructure<TeacherResponse>> getTeacherById(String id) {
		logger.info("Attempting to get a teacher with Id: {}", id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to add teacher.");
			throw new UserIsNotLoginException("user Is Not Login");
		}
		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return teacherRepo.findById(id).map(teacher -> {
			logger.debug("teacher data found :", teacher);
			return ResponseEntity.ok(teachersResponse.setStatusCode(HttpStatus.OK.value()).setMessage("data found")
					.setBody(maptoTeacherResponse(teacher)));
		}).orElseThrow(() -> new TeacherIdNotFoundException("Teacher Not fund By Given Id"));

	}

	@Override
	public ResponseEntity<ResponseStructure<List<TeacherResponse>>> getAllTeachers() {
		logger.info("Attempting to get all teacher with : {}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to add teacher.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {
			logger.error("Unauthorized user: {} attempted to All teachers", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}

		List<Teacher> findAll = teacherRepo.findAllByRole(Role.TEACHER);
		if (findAll.isEmpty()) {
			logger.error("No data Found");
			throw new NoTeachersFoundException("No Teachers Found");
		}
		return ResponseEntity.ok(teachers.setStatusCode(HttpStatus.FOUND.value()).setMessage("Teachers Data Found")
				.setBody(mapToTeachres(findAll)));
	}

	@Override
	public ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher(TeacherUpdateRequest teacherReuest,
			String id) {
		logger.info("Attempting to Update the teacher Data with Id: {}", id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("unauthorized....... ");
			;
			throw new UserIsNotLoginException("user Is Not Login");
		}
		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {
			logger.error("Unauthorized user: {} attempted to update teacher", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		return teacherRepo.findByIdAndRole(id, Role.TEACHER).map(teacher -> {
			teacher = mapToUpdateTeacher(teacherReuest, teacher);

			teacher = teacherRepo.save(teacher);
			logger.debug("teacher data Updated :", teacher);
			return ResponseEntity.ok(teachersResponse.setStatusCode(HttpStatus.OK.value()).setMessage("Data Updated")
					.setBody(maptoTeacherResponse(teacher)));
		}).orElseThrow(() -> new TeacherIdNotFoundException("Invalid TeacherId or Teacher Is not Present"));

	}

	@Override
	public ResponseEntity<SimpleResponseStructure> deleteTeacher(String id) {
		logger.info("Attempting to delete  teacher with Id: {}", id);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to delete teacher.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {
			logger.error("Unauthorized user: {} attempted to delete teacher", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		return teacherRepo.findByIdAndRole(id, Role.TEACHER).map(teacher -> {

			List<Student> students = studentRepo.findAllByTeacherIdAndRole(teacher.getId(), Role.STUDENT);
			if (!students.isEmpty()) {
				students = removeTeacherInStudent(students);
				studentRepo.saveAll(students);
				teacherRepo.delete(teacher);
				return ResponseEntity
						.ok(simplStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Teacher Is Removed"));
			} else {
				teacherRepo.delete(teacher);
				logger.debug("teacher data deleted :");
				return ResponseEntity
						.ok(simplStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Teacher Is Removed"));

			}

		}).orElseThrow(() -> new TeacherIdNotFoundException("Invalid Teacher Id or Id Not Present"));

	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> addStudent(StudentRequest studentRequest) {
		logger.info("Attempting to add a Student : {}", studentRequest);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {

			logger.error("Unauthorized access attempt to add Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {

			logger.error("Unauthorized user: {} attempted to Add Studet", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		if (studentRepo.existsByUsername(studentRequest.getEmail().split("@")[0])) {

			logger.error("Unauthorized Operation: {} attempted to add Student ", username);
			throw new StudentUsernameAllReadyPresentException("username AllReady Present give different username");
		}
		Student student = mapToStudent(studentRequest, new Student());
		student = studentRepo.save(student);

		logger.debug("Student data saved :", student);
		return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
				.setMessage("Student Added SuccessFully").setBody(teacherServiceImpl.mapToStudentResponse(student)));
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(String sId) {

		logger.info("Attempting to get a Student with Id: {}", sId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {

			logger.error("Unauthorized access attempt to get student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();

		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {

			logger.error("Unauthorized user: {} attempted to get student", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		return studentRepo.findByIdAndRole(sId, Role.STUDENT).map(student -> {
			StudentResponse studentResponse = teacherServiceImpl.mapToStudentResponse(student);

			logger.debug("teacher data found :", student);
			return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("data Found").setBody(studentResponse));

		}).orElseThrow(() -> new StudentNotFoundByIdException("Id not Found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(
			@Valid StudentUpdateRequest stuUpdateRequest, String sId) {

		logger.info("Attempting to update a Student with Id: {}", sId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {

			logger.error("Unauthorized access attempt to update Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();

		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {

			logger.error("Unauthorized access attempt to update Student.");
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		return studentRepo.findByIdAndRole(sId, Role.STUDENT).map(student -> {

			student = mapToStudent(stuUpdateRequest, student);
			studentRepo.save(student);
			logger.debug("Student data updated :", student);
			return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("Data found ").setBody(teacherServiceImpl.mapToStudentResponses(student)));
		}).orElseThrow(() -> new StudentNotFoundByIdException("stundet Not Found By Given Id or Invalid Id"));

	}

	@Override
	public ResponseEntity<SimpleResponseStructure> deleteStudent(String sId) {
		logger.info("Attempting to get a student with Id: {}", sId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to delete Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {
			logger.error("Unauthorized user: {} attempted to update teacher", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		return studentRepo.findById(sId).map(student -> {
			studentRepo.delete(student);
			logger.debug("Student data is deleted :", student);
			return ResponseEntity.ok(simplStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Data removed"));
		}).orElseThrow(() -> new StudentNotFoundByIdException(" Ivalid Id"));

	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudents() {
		logger.info("Attempting to get all Student: {}");
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {
			logger.error("Unauthorized access attempt to get ALl Student.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		logger.debug("Authenticated user: {}", username);
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {
			logger.error("Unauthorized user: {} attempted to Get ALl Students", username);
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}
		List<Student> students = studentRepo.findByRole(Role.STUDENT);
		if (students.isEmpty()) {
			logger.error("no Students Found");
			throw new NoStudentsFoundException("no Students Found......");
		}
		logger.debug("Data Found :", students);
		return ResponseEntity.ok(studentResponses.setStatusCode(HttpStatus.OK.value()).setMessage("data Found")
				.setBody(teacherServiceImpl.mapToStudentResponses(students)));
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> addTeacherToStudent(String sId, String teacherId) {

		logger.info("Attempting to add Teacher To  Student with Ids: {}", teacherId, sId);
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated()) {

			logger.error("Unauthorized access attempt to  add Teacher To Students.");
			throw new UserIsNotLoginException("user Is Not Login");
		}

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name())) {

			logger.error("Unauthorized access attempt to  add Teacher To Students.");
			throw new UnauthorizedException("Only Admin Can Access This Page");
		}

		return teacherRepo.findByIdAndRole(teacherId, Role.TEACHER).map(teacher -> {
			return studentRepo.findByIdAndRole(sId, Role.STUDENT).map(student -> {
				if (student.getTeacherId() == null) {
					student.setTeacherId(Arrays.asList(teacherId));
				} else
					student.getTeacherId().add(teacherId);
				student = studentRepo.save(student);

				logger.debug("Student data Updated :", student);
				return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("data found").setBody(teacherServiceImpl.mapToStudentResponse(student)));
			}).orElseThrow(() -> new StudentNotFoundByIdException("invalid Student Id"));
		}).orElseThrow(() -> new TeacherIdNotFoundException("invalid Teacher Id"));
	}

//************************************ Helper Methods *************************************************************************************

	private TeacherResponse maptoTeacherResponse(Teacher teacher) {
		return TeacherResponse.builder().name(teacher.getName()).username(teacher.getUsername())
				.subject(teacher.getSubject()).role(teacher.getRole()).teacherId(teacher.getId()).build();

	}

	private Teacher mapToTeacher(TeacherReuest teacherRequest, Teacher teacher) {

		teacher.setName(teacherRequest.getName());
		teacher.setUsername(teacherRequest.getEmail().split("@")[0]);
		teacher.setPassword(encoder.encode(teacherRequest.getPassword()));
		teacher.setSubject(teacherRequest.getSubject());
		teacher.setRole(Role.TEACHER);
		return teacher;
	}

	private List<TeacherResponse> mapToTeachres(List<Teacher> findAll) {
		List<TeacherResponse> teachers = new ArrayList<>();
		for (Teacher t : findAll) {
			teachers.add(maptoTeacherResponse(t));
		}
		return teachers;
	}

	private Teacher mapToUpdateTeacher(TeacherUpdateRequest teacherReuest, Teacher teacher) {
		teacher.setId(teacher.getId());
		if (teacherReuest.getName() != null)
			teacher.setName(teacherReuest.getName());
		if (teacherReuest.getEmail() != null)
			teacher.setUsername(teacherReuest.getEmail().split("@")[0]);
		if (teacherReuest.getPassword() != null)
			teacher.setPassword(teacherReuest.getPassword());
		if (teacherReuest.getSubject() != null)
			teacher.setSubject(teacherReuest.getSubject());

		return teacher;
	}

	private List<Student> removeTeacherInStudent(List<Student> students) {
		for (Student s : students) {
			s.setTeacherId(null);
		}
		return students;
	}

	private Student mapToStudent(StudentRequest studentRequest, Student student) {
		student.setName(studentRequest.getName());
		student.setUsername(studentRequest.getEmail().split("@")[0]);
		student.setRole(Role.STUDENT);
		student.setPassword(encoder.encode(studentRequest.getPassword()));
		student.setMarks(Arrays.asList(studentRequest.getMarks()));

		return student;

	}

	private Student mapToStudent(StudentUpdateRequest request, Student student) {
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

}
