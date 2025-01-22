package com.cms.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	private UserRepository userRepo;
	private TeacherRepository teacherRepo;
	private ResponseStructure<TeacherResponse> teachersResponse;
	private ResponseStructure<List<TeacherResponse>> teachers;
	private StudentRepository studentRepo;
	private SimpleResponseStructure simplStructure;
	private PasswordEncoder encoder;
	private TeacherServiceImpl teacherServiceImpl;
	private ResponseStructure<StudentResponse> studentResponseStructure;
	private SimpleResponseStructure simpleResponseStructure;
	private ResponseStructure<List<StudentResponse>> studentResponses;

	@Override
	public ResponseEntity<ResponseStructure<TeacherResponse>> addTeacher(TeacherReuest teacherRequest) {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		return userRepo.findByUsername(username).map(user -> {
			if (!user.getRole().name().equals(Role.ADMIN.name()))
				throw new UnauthorizedException("Only Admin Can Access This Page");

			if (teacherRepo.findByUsername(teacherRequest.getEmail().split("@")[0]).isPresent())
				throw new EmailAllReadyPresentexception("email allready Present");
			Teacher teacher = mapToTeacher(teacherRequest, new Teacher());

			teacher.setSubject(teacherRequest.getSubject());
			teacher = teacherRepo.save(teacher);
			System.out.println("data stored==================");
			return ResponseEntity.ok(teachersResponse.setStatusCode(HttpStatus.CREATED.value())
					.setMessage("Teacher is Added").setBody(maptoTeacherResponse(teacher)));

		}).orElseThrow(() -> new UnauthorizedException("unauthorized page"));

	}

	private TeacherResponse maptoTeacherResponse(Teacher teacher) {
		return TeacherResponse.builder().name(teacher.getName()).username(teacher.getUsername())
				.subject(teacher.getSubject()).role(teacher.getRole()).build();

	}

	private Teacher mapToTeacher(TeacherReuest teacherRequest, Teacher teacher) {

		teacher.setName(teacherRequest.getName());
		teacher.setUsername(teacherRequest.getEmail().split("@")[0]);
		teacher.setPassword(encoder.encode(teacherRequest.getPassword()));
		teacher.setSubject(teacherRequest.getSubject());
		teacher.setRole(Role.TEACHER);
		return teacher;
	}

	@Override
	public ResponseEntity<ResponseStructure<TeacherResponse>> getTeacherById(String id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return teacherRepo.findById(id).map(teacher -> {

			return ResponseEntity.ok(teachersResponse.setStatusCode(HttpStatus.OK.value()).setMessage("data found")
					.setBody(maptoTeacherResponse(teacher)));
		}).orElseThrow(() -> new TeacherIdNotFoundException("Teacher Not fund By Given Id"));

	}

	@Override
	public ResponseEntity<ResponseStructure<List<TeacherResponse>>> getAllTeachers() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		List<Teacher> findAll = teacherRepo.findAllByRole(Role.TEACHER);
		if (findAll.isEmpty())
			throw new NoTeachersFoundException("No Teachers Found");
		return ResponseEntity.ok(teachers.setStatusCode(HttpStatus.FOUND.value()).setMessage("Teachers Data Found")
				.setBody(mapToTeachres(findAll)));
	}

	private List<TeacherResponse> mapToTeachres(List<Teacher> findAll) {
		List<TeacherResponse> teachers = new ArrayList<>();
		for (Teacher t : findAll) {
			teachers.add(maptoTeacherResponse(t));
		}
		return teachers;
	}

	@Override
	public ResponseEntity<ResponseStructure<TeacherResponse>> updateTeacher(TeacherUpdateRequest teacherReuest,
			String id) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");
		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return teacherRepo.findById(id).map(teacher -> {
			teacher = mapToUpdateTeacher(teacherReuest, teacher);

			teacher = teacherRepo.save(teacher);
			return ResponseEntity.ok(teachersResponse.setStatusCode(HttpStatus.OK.value()).setMessage("Data Updated")
					.setBody(maptoTeacherResponse(teacher)));
		}).orElseThrow(() -> new TeacherIdNotFoundException("Invalid TeacherId or Teacher Is not Present"));

	}

	private Teacher mapToUpdateTeacher(TeacherUpdateRequest teacherReuest, Teacher teacher) {
		// TODO Auto-generated method stub
		teacher.setId(teacher.getId());
		if (!teacherReuest.getName().isEmpty())
			teacher.setName(teacherReuest.getName());
		if (!teacherReuest.getEmail().isEmpty())
			teacher.setUsername(teacherReuest.getEmail().split("@")[0]);
		if (!teacherReuest.getPassword().isEmpty())
			teacher.setPassword(teacherReuest.getPassword());
		if (!teacherReuest.getSubject().isEmpty())
			teacher.setSubject(teacherReuest.getSubject());

		return teacher;
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> deleteTeacher(String id) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return teacherRepo.findById(id).map(teacher -> {

			List<Student> students = studentRepo.findAllByTeacherId(teacher.getId());
			if (!students.isEmpty()) {
				students = removeTeacherInStudent(students);
				studentRepo.saveAll(students);
				teacherRepo.delete(teacher);
				return ResponseEntity
						.ok(simplStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Teacher Is Removed"));
			} else {
				teacherRepo.delete(teacher);
				return ResponseEntity
						.ok(simplStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Teacher Is Removed"));

			}

		}).orElseThrow(() -> new TeacherIdNotFoundException("Invalid Teacher Id or Id Not Present"));

	}

	private List<Student> removeTeacherInStudent(List<Student> students) {
		// TODO Auto-generated method stub
		for (Student s : students) {
			s.setTeacherId(null);
		}
		return students;
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> addStudent(StudentRequest studentRequest) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");
		if (studentRepo.existsByUsername(studentRequest.getEmail().split("@")[0]))
			throw new StudentUsernameAllReadyPresentException("username AllReady Present give different username");
		Student student = mapToStudent(studentRequest, new Student());
		studentRepo.save(student);
		return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
				.setMessage("Student Added SuccessFully").setBody(teacherServiceImpl.mapToStudentResponse(student)));
	}

	private Student mapToStudent(StudentRequest studentRequest, Student student) {
		student.setName(studentRequest.getName());
		student.setUsername(studentRequest.getEmail().split("@")[0]);
		student.setRole(Role.STUDENT);
		student.setPassword(encoder.encode(studentRequest.getPassword()));
		student.setMarks(Arrays.asList(studentRequest.getMarks()));

		return student;

	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> getStudent(String sId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return studentRepo.findById(sId).map(student -> {
			StudentResponse studentResponse = teacherServiceImpl.mapToStudentResponse(student);
			return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
					.setMessage("data Found").setBody(studentResponse));

		}).orElseThrow(() -> new StudentNotFoundByIdException("Id not Found"));
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> updateStudent(
			@Valid StudentUpdateRequest stuUpdateRequest, String sId) {

		return teacherServiceImpl.updateStudents(stuUpdateRequest, sId);

	}

	@Override
	public ResponseEntity<SimpleResponseStructure> deleteStudent(String sId) {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return studentRepo.findById(sId).map(student -> {
			studentRepo.delete(student);
			return ResponseEntity.ok(simplStructure.setStatusCode(HttpStatus.OK.value()).setMessage("Data removed"));
		}).orElseThrow(() -> new StudentNotFoundByIdException(" Ivalid Id"));

	}

	@Override
	public ResponseEntity<ResponseStructure<List<StudentResponse>>> getAllStudents() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");
		List<Student> students = studentRepo.findAll();
		if (students.isEmpty())
			throw new NoStudentsFoundException("no Students Found......");

		return ResponseEntity.ok(studentResponses.setStatusCode(HttpStatus.OK.value()).setMessage("data Found")
				.setBody(teacherServiceImpl.mapToStudentResponses(students)));
	}

	@Override
	public ResponseEntity<ResponseStructure<StudentResponse>> addTeacherToStudent(String sId, String teacherId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!authentication.isAuthenticated())
			throw new UserIsNotLoginException("user Is Not Login");

		String username = authentication.getName();
		User user = userRepo.findByUsername(username).get();
		if (!user.getRole().name().equals(Role.ADMIN.name()))
			throw new UnauthorizedException("Only Admin Can Access This Page");

		return teacherRepo.findById(teacherId).map(teacher -> {
			return studentRepo.findById(sId).map(student -> {
				if (student.getTeacherId().isEmpty())
					student.setTeacherId(Arrays.asList(teacherId));
				else
					student.getTeacherId().add(teacherId);
				studentRepo.save(student);
				return ResponseEntity.ok(studentResponseStructure.setStatusCode(HttpStatus.OK.value())
						.setMessage("data found").setBody(teacherServiceImpl.mapToStudentResponse(student)));
			}).orElseThrow(() -> new StudentNotFoundByIdException("invalid Student Id"));
		}).orElseThrow(() -> new TeacherIdNotFoundException("invalid Teacher Id"));
	}

}
