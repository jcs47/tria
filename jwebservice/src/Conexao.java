
import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.*;

public class Conexao {

	static String status = "";
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//String url = "jdbc:mysql://localhost/classicmodels?user=root&password=";
			String url = "jdbc:mysql://169.254.83.95/classicmodels?user=root&password=";
			conn = DriverManager.getConnection(url);
			status = "Conexão aberta";
		} catch(ClassNotFoundException e) {
			status = e.getMessage();
		} catch(SQLException e) {
			status = e.getMessage();
		} catch(Exception e) {
			status = e.getMessage();
		}
		return conn;
	}
	
	public static void main(String[] args) throws IOException, NotBoundException {
		Connection conn = Conexao.getConnection();
	}
}

