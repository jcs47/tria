

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
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class OfficesBase
 */
public class OfficesBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String DEFAULT_STRING = "null";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OfficesBase() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Logger.log("REQUEST", "LIST-BASE");
		// Set the HTTP Headers
		response.setContentType("application/json");
		response.addHeader("Access-Control-Allow-Origin", "*");
		
		// Get The response
		MessageResponse m = new MessageResponse();
		JSONObject jsonResponse = new JSONObject();
		
		try {
                        // Used with RMI	
                        /*Registry reg = LocateRegistry.getRegistry("10.10.5.150", 1099);
			OfficeDAOInterface auth = (OfficeDAOInterface) reg.lookup("officeProxyDB");
                        m = auth.getAllOfficeBase();*/
			
                        // Used without RMI (DB code in the proxy)
                        OfficeDAO auth = new OfficeDAO();
                        m = auth.getAllOfficeBase();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} /*catch (NotBoundException e) { // Exception thrown if RMI is used
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		if(m.getStatus().equals("OK")) {
			ArrayList<Office> officesArray = m.getRS();
			JSONArray officesJSON = new JSONArray();
			for (Office o : officesArray) {
				JSONObject oJSON = new JSONObject();

				if(o.getOfficeCode() != null) {
					oJSON.put("officeCode", o.getOfficeCode());
				}
				else {
					oJSON.put("officeCode", DEFAULT_STRING);
				}

				if(o.getCity() != null) {
					oJSON.put("city", o.getCity());
				} else {
					oJSON.put("city", DEFAULT_STRING);
				}

				if((o.getPhone() != null)) {
					oJSON.put("phone", o.getPhone());
					//oJSON.put("phone", "XXX");

				} else {
					oJSON.put("phone", DEFAULT_STRING);
				}

				if(o.getAddressLine1() != null) {
					oJSON.put("addressLine1", o.getAddressLine1());
				} else {
					oJSON.put("addressLine1", DEFAULT_STRING);
				}

				if(o.getAddressLine2() != null) {
					oJSON.put("addressLine2", o.getAddressLine2());
				} else {
					oJSON.put("addressLine2", DEFAULT_STRING);
				}

				if(o.getState() != null) {
					oJSON.put("state", o.getState());
				} else {
					oJSON.put("state", DEFAULT_STRING);
				}

				if(o.getCountry() != null) {
					oJSON.put("country", o.getCountry());
				} else {
					oJSON.put("country", DEFAULT_STRING);
				}

				if(o.getPostalCode() != null) {
					oJSON.put("postalCode", o.getPostalCode());
				} else {
					oJSON.put("postalCode", DEFAULT_STRING);
				}

				if(o.getTerritory() != null) {
					oJSON.put("territory", o.getTerritory());
				} else {
					oJSON.put("territory", DEFAULT_STRING);
				}
				officesJSON.put(oJSON);
			}

			/** The Response **/
			jsonResponse.put("Status", m.getStatus());
			jsonResponse.put("RS", officesJSON);
		}
		else {
			jsonResponse.put("Status", m.getStatus());
		}
		
		PrintWriter out = response.getWriter();
		out.println(jsonResponse.toString());
		Logger.log("RESPONSE", "LIST-BASE");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
