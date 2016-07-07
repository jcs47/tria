

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
 * Servlet implementation class Login
 */
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.log("REQUEST", "LOGIN");
		
		// Set the Response's Headers
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		// Get the Request's Parameters
		String id = request.getParameter("ID");
		String Sa = request.getParameter("Sa");
		String h = request.getParameter("h");
		
		// Authentication
		JSONObject jsonResponse = new JSONObject();
		PrintWriter out = response.getWriter();
		MessageAuthentication m = null;
		try {
			Registry reg = LocateRegistry.getRegistry("169.254.83.95", 1099);
			AuthenticatorInterface auth = (AuthenticatorInterface) reg.lookup("authenticatorProxyDB");
			m = auth.doLogin(id, Sa, h);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(m != null) {
			jsonResponse.put("STATUS", m.getStatus());
			jsonResponse.put("ID", m.getId());
			jsonResponse.put("EKsT", m.getEKsT());
			jsonResponse.put("Sa", m.getSa());
			//jsonResponse.put("Sa", "xxx");
			jsonResponse.put("Sb", m.getSb());
			jsonResponse.put("signature", m.getSign());
			System.out.println(m.getStatus());
			out.println(jsonResponse.toString());
		}
		else {
			response.setStatus(401);
		}
		Logger.log("RESPONSE", "LOGIN");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Set the Response's Headers
				response.setContentType("application/json");
				response.addHeader("Access-Control-Allow-Origin", "*");
				
				// Get the Request's Parameters
				String id = request.getParameter("ID");
				String Sa = request.getParameter("Sa");
				String h = request.getParameter("h");
				
				// Authentication
				JSONObject jsonResponse = new JSONObject();
				PrintWriter out = response.getWriter();
				MessageAuthentication m = null;
				try {
					m = Authenticator.doLogin(id, Sa, h);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SignatureException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(m != null) {
					jsonResponse.put("ID", m.getId());
					jsonResponse.put("EKsT", m.getEKsT());
					jsonResponse.put("Sa", m.getSa());
					jsonResponse.put("Sb", m.getSb());
					jsonResponse.put("signature", m.getSign());
					out.println(jsonResponse.toString());
				}
				else {
					response.setStatus(401);
				}
	}

}
