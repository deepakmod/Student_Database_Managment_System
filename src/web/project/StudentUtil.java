package web.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentUtil {
	
	private DataSource dataSource;
	
	StudentUtil(DataSource theDataSource){
		dataSource = theDataSource;
	}
	
	public List<Student>getStudent(){
		List <Student> students = new ArrayList<>();
		
		Connection con=null;
		Statement sta=null;
		ResultSet res=null;
		
		try {
			con=dataSource.getConnection();
			String qua="SELECT *FROM student";
			sta=con.createStatement();
			res=sta.executeQuery(qua);
			
			while(res.next()) {
				int id = res.getInt("id");
				String firstName=res.getString("first_name");
				String lastName=res.getString("last_name");
				String email=res.getString("email");
				
				Student tempStudent = new Student(id,firstName,lastName,email);
					students.add(tempStudent);
			}
		}
		catch(Exception exe) {
			exe.printStackTrace();
		}
		finally {
			close(con,sta,res);
		}
		return students;
	}
	
	public Student getStudent(String theStudentId)throws Exception{
		Student theStudent=null;
		
		Connection con=null;
		PreparedStatement sta=null;
		ResultSet res=null;
		
		int StudentId;
		try {
		StudentId = Integer.parseInt(theStudentId);
		con=dataSource.getConnection();
		String sql = "select *from student where id=?";
		sta = con.prepareStatement(sql);
		sta.setInt(1,StudentId);
		res=sta.executeQuery();
		
		if(res.next()) {
			String firstName=res.getString("first_name");
			String lastName=res.getString("last_name");
			String email=res.getString("email");
			
			theStudent = new Student(StudentId,firstName,lastName,email);
			}
		else {
			throw new Exception("Could Not Find Student id "+StudentId);
		}
		}finally {
			close(con,sta,res);
		}
		return theStudent;
		
	}
	
	public void addStudent(Student theStudent)throws Exception{
		Connection con=null;
		PreparedStatement sta=null;
		try {
			con=dataSource.getConnection();
			String str = "insert into student (first_name,last_name,email)values(?,?,?)";
			sta = con.prepareStatement(str);
			sta.setString(1, theStudent.getFirstName());
			sta.setString(2, theStudent.getLastName());
			sta.setString(3, theStudent.getEmail());
			
			sta.execute();
			
		}
		finally {
			close(con,sta,null);
		}
	}



	public void deleteStudent(String theStudentId)throws Exception  {
		Connection con=null;
		PreparedStatement sta=null;

		int StudentId;
		try {
		StudentId = Integer.parseInt(theStudentId);
		con=dataSource.getConnection();
		String sql = "DELETE FROM student WHERE id=?";
		sta = con.prepareStatement(sql);
		sta.setInt(1,StudentId);
		sta.execute();
		
		}finally {
			close(con,sta,null);
		}
	}

	public void updateStudent(Student theStudent)throws Exception {
		Connection con=null;
		PreparedStatement sta=null;
		try {
			con=dataSource.getConnection();
			String str = "UPDATE student SET first_name=?,last_name=?,email=? WHERE id=?";
			sta = con.prepareStatement(str);
			sta.setString(1,theStudent.getFirstName());
			sta.setString(2,theStudent.getLastName());
			sta.setString(3,theStudent.getEmail());
			sta.setInt(4,theStudent.getId());
			sta.execute();
			
		}
		finally {
			close(con,sta,null);
		}
	}

	private void close(Connection con, Statement sta, ResultSet res) {
		try {
			if(con!=null)
				con.close();
			if(sta!=null)
				sta.close();
			if(res!=null)
				res.close();
			}
		catch(Exception exe) {
			exe.printStackTrace();
			}
		}
}