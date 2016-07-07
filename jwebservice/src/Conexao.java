
import java.io.IOException;
import java.rmi.NotBoundException;
import java.sql.*;

public class Conexao {

	static String status = "";
	
        //BFT info
	private static String connectionURL = "jdbc:bftdriver;classicmodels0;classicmodels1;classicmodels2;classicmodels3";
	private static String username = "root;root;root;root";
	private static String password = "rammstein;rammstein;rammstein;rammstein";

    public static Connection getSingleConnection(int id) {

        Connection conn = null;

        try {
            //Vanilla DB
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://localhost:3306/classicmodels" + id + "?user=root&password=rammstein";
            conn = DriverManager.getConnection(url);

        } catch (ClassNotFoundException e) {
            status = e.getMessage();
            System.out.println(status);
            e.printStackTrace();
        } catch (SQLException e) {
            status = e.getMessage();
            System.out.println(status);
            e.printStackTrace();
        } catch (Exception e) {
            status = e.getMessage();
            System.out.println(status);
            e.printStackTrace();
        }
        return conn;
    }

        public static Connection getConnection() {
            
                System.setProperty("divdb.folder", "/home/snake/Desktop/git/steeldb/config");
		Connection conn = null;

		try {
                                        
                        //Useless
                        //////String url = "jdbc:mysql://169.254.83.95/classicmodels?user=root&password=";
                    
                        //BFT DB
			Class.forName("lasige.steeldb.jdbc.BFTDriver");
			conn = DriverManager.getConnection(connectionURL, username, password);
                        
			status = "Conexao aberta";
		} catch(ClassNotFoundException e) {
			status = e.getMessage();
                        System.out.println(status);
                        e.printStackTrace();
		} catch(SQLException e) {
			status = e.getMessage();
                        System.out.println(status);
                        e.printStackTrace();
		} catch(Exception e) {
			status = e.getMessage();
                        System.out.println(status);
                        e.printStackTrace();
		}

		return conn;
	}
}

