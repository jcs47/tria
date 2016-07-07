

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

import org.json.JSONException;
import org.json.JSONObject;

public class DeleteOffice extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeleteOffice() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.log("REQUEST", "DELETE");
		
		// Set the HTTP Headers
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		// Get the Request's Parameters
		String id = request.getParameter("ID");
		String TOp = request.getParameter("TOp");
		String idOffice = request.getParameter("D");
		String mac = request.getParameter("MAC");

		// Get The response
		MessageResponse m = new MessageResponse();
		try {
			Registry reg = LocateRegistry.getRegistry("169.254.83.95", 1099);
			OfficeDAOInterface auth = (OfficeDAOInterface) reg.lookup("officeProxyDB");
			m = auth.deleteOffice(id, TOp, idOffice, mac);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		JSONObject jsonResponse = new JSONObject();
		if(m.getStatus().equals("OK")) {
			/** The Response **/
			jsonResponse.put("Status", m.getStatus());
			jsonResponse.put("ID", m.getId());
			jsonResponse.put("TOp", m.getTOp());
			jsonResponse.put("RS", m.getRS().get(0));
			jsonResponse.put("EKsT", m.getEKsT());
			jsonResponse.put("signature", m.getSignature());
		}
		else {
			jsonResponse.put("Status", m.getStatus());
			jsonResponse.put("ID", m.getId());
			jsonResponse.put("TOp", m.getTOp());
			jsonResponse.put("signature", m.getSignature());
		}
		PrintWriter out = response.getWriter();
		out.println(jsonResponse.toString());
		Logger.log("RESPONSE", "DELETE");
	}

}
