

import java.io.IOException;
import java.io.PrintWriter;
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

/**
 * Servlet implementation class InsertOfficeBase
 */
public class InsertOfficeBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InsertOfficeBase() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.log("REQUEST", "INSERT-BASE");
		// Set the HTTP Headers
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");

		String officeJson = request.getParameter("D");

		// Get The response
		MessageResponse m = new MessageResponse();
		JSONObject jsonResponse = new JSONObject();
		try {
			m = (new OfficeDAO()).insertOfficeBase(officeJson);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(m.getStatus().equals("OK")) {
			/** The Response **/
			jsonResponse.put("Status", m.getStatus());
			jsonResponse.put("RS", m.getRS().get(0));
		}
		else {
			jsonResponse.put("Status", m.getStatus());
		}
		PrintWriter out = response.getWriter();
		out.println(jsonResponse.toString());
		Logger.log("RESPONSE", "INSERT-BASE");
	}

}
