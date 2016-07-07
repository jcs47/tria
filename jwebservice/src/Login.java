

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
import org.json.JSONArray;

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
		//String h = request.getParameter("h");
                
                String[] Hi = new String[Authenticator.N];
                for (int i = 0; i < Hi.length; i++)
                    Hi[i] = request.getParameter("Hi" + i);
		
		// Authentication
		JSONObject jsonResponse = new JSONObject();
		PrintWriter out = response.getWriter();
		MessageAuthentication[] m = new MessageAuthentication[Authenticator.N];
                
		try {
                    
                    // used without RMI (DB code in the proxy)
                    /*Authenticator auth = new Authenticator();
                    m = auth.doLogin(id, Sa, h, Hi);*/
                    
                    // Used with RMI
                    //TODO: paralelizar?
                    for (int i = 0; i < Authenticator.N; i++) {
                        Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099 + i);
                        AuthenticatorInterface auth = (AuthenticatorInterface) reg.lookup("authenticatorProxyDB" + i);
                        m[i] = auth.doLogin(id, Sa, Hi[i]);
                    }
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
		} catch (NotBoundException e) { // Exception thrown if RMI is used
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(m != null) {
                    
                    //TODO: proxy should do some checking, maybe
			jsonResponse.put("STATUS", m[0].getStatus());
			jsonResponse.put("ID", m[0].getId());
			//jsonResponse.put("EKsT", m[0].getEKsT());
			jsonResponse.put("Sa", m[0].getSa());
                        
                        JSONArray sb = new JSONArray();
                        JSONArray ekst = new JSONArray();
                        JSONArray sigs = new JSONArray();
                        for (int i = 0; i < Authenticator.N; i++) {
                            sb.put(m[i].getSb());
                            ekst.put(m[i].getEKsT());
                            sigs.put(m[i].getSign());
                        }
			jsonResponse.put("Sb", sb.toString());
                        jsonResponse.put("EKsT", ekst.toString());
			jsonResponse.put("signature", sigs.toString());
			
                        System.out.println(m[0].getStatus());
                        System.out.println(sb.toString());
                        System.out.println(sigs.toString());
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
				
                                String[] Hi = new String[Authenticator.N];
                                for (int i = 0; i < Hi.length; i++)
                                    Hi[i] = request.getParameter("Hi" + i);
                
                                // Authentication
				JSONObject jsonResponse = new JSONObject();
				PrintWriter out = response.getWriter();
				MessageAuthentication m = null;
				try {
                                    for (int i = 0; i < Authenticator.N; i++)
					m = (new Authenticator(i)).doLogin(id, Sa, h);
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
