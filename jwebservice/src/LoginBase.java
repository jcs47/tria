

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

/**
 * Servlet implementation class LoginBase
 */
public class LoginBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginBase() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.log("REQUEST", "LOGIN-BASE");
		
		// Set the Response's Headers
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		// Get the Request's Parameters
		String id = request.getParameter("ID");
		String p = request.getParameter("P");
		
		// Authentication
		JSONObject jsonResponse = new JSONObject();
		PrintWriter out = response.getWriter();
		MessageAuthentication m = null;
		try {
			Registry reg = LocateRegistry.getRegistry("10.10.5.150", 1099);
			AuthenticatorInterface auth = (AuthenticatorInterface) reg.lookup("authenticatorProxyDB");
			m = auth.doLoginBase(id, p);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(m != null) {
			jsonResponse.put("STATUS", m.getStatus());
			out.println(jsonResponse.toString());
		}
		else {
			response.setStatus(401);
		}
		Logger.log("RESPONSE", "LOGIN-BASE");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
