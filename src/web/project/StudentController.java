package web.project;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/StudentController")
public class StudentController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentUtil studentUtil;
	@Resource(name="jdbc/student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init()throws ServletException{
		super.init();
		try {
			studentUtil = new StudentUtil(dataSource);
		}catch(Exception exc) {
			throw new ServletException(exc);
		}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String theCommand = request.getParameter("command");
			if(theCommand==null) {
				theCommand ="LIST";	
			}
			
			switch(theCommand) {
			
			case "LIST":
				listStudents(request,response);
				break;
			case "ADD":
				addStudent(request,response);
				break;
			case "LOAD":
				loadStudent(request,response);
				break;
			case "UPDATES":
				updateStudent(request,response);
				break;
			case "DELETE":
				deleteStudent(request,response);
				break;
			default:
				listStudents(request,response);
				break;
			}
		}catch(Exception exc) {
			throw new ServletException(exc);
		}
	}

	private void listStudents(HttpServletRequest request, HttpServletResponse response) throws Exception{
		List <Student>students = studentUtil.getStudent();
		
		request.setAttribute("STUDENT_LIST", students);
		
		RequestDispatcher rd = request.getRequestDispatcher("/list-student.jsp");
		rd.forward(request, response);
	}
	
	private void addStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(firstName,lastName,email);
		
		studentUtil.addStudent(theStudent);
		
		listStudents(request,response);
	}
	
	private void loadStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String theStudentId = request.getParameter("studentId");
		
		Student theStudent = studentUtil.getStudent(theStudentId);
		
		request.setAttribute("THE_STUDENT", theStudent);
		
		RequestDispatcher rd = request.getRequestDispatcher("/update-student.jsp");
		rd.forward(request, response);
	}
	
	private void deleteStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String theStudentId = request.getParameter("studentId");
		
		studentUtil.deleteStudent(theStudentId);
		
		listStudents(request, response);
	}
	
	private void updateStudent(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int id = Integer.parseInt(request.getParameter("studentId"));
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String email = request.getParameter("email");
		
		Student theStudent = new Student(id,firstName,lastName,email);
		
		studentUtil.updateStudent(theStudent);
		
		listStudents(request,response);	
	}

}
