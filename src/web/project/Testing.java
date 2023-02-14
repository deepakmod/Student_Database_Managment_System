package web.project;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

	@WebServlet("/Testing")
	public class Testing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Resource(name="jdbc/student_tracker")
    private DataSource dataSource;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		response.setContentType("text/plain");
		
		Connection con=null;
		Statement sta=null;
		ResultSet res=null;
		
		try {
			con=dataSource.getConnection();
			String qry="SELECT *FROM student";
			sta=con.createStatement();
			res=sta.executeQuery(qry);
			
			while(res.next()) {
				String email=res.getString("email");
				out.println(email);
			}
			
		}catch(Exception exe) {
			exe.printStackTrace();
		}
		
		
	}

}
