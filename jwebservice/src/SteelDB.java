/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
//import lasige.steeldb.comm.TRIAReply;
import org.json.JSONObject;

/**
 *
 * @author joao
 */
public class SteelDB extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    public SteelDB() {
        super();
    
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Logger.log("REQUEST", "STEELDB");
            
        // Set the HTTP Headers
        response.setContentType("application/json");
        response.addHeader("Access-Control-Allow-Origin", "*");
            
        // Get the Request's Parameters
        String id = request.getParameter("ID");
        String TOp = request.getParameter("TOp");
        String cmd = request.getParameter("CMD");
        //String hmac = request.getParameter("MAC");

        String[] macs = new String[Authenticator.N];
        for (int i = 0; i < macs.length; i++)
            macs[i] = request.getParameter("Mi" + i);

        // Get The response
        MessageResponse m = new MessageResponse();
        JSONObject jsonResponse = new JSONObject();
            
        /////////////////////// TEMPORARIO ///////////////////////////
        
        String Ks = null;
        String[] temp = new String[macs.length];
        long token = 0;
        
        Connection conn = null;
        Statement stm = null;
            
        try {
            
            for (int i = 0; i < temp.length; i++) {
                
                /**
                * Connect to Database *
                */
                conn = Conexao.getSingleConnection(i);
                stm = conn.createStatement();
                /**
                * Get Session Key and Token *
                */
                String sqlToken = "SELECT Ks, token FROM users WHERE id = '" + id + "'";
                ResultSet rsToken = stm.executeQuery(sqlToken);
                if (rsToken.next()) {
                    Ks = rsToken.getString("Ks");
                    token = rsToken.getLong("token");
                }
                        
                /**
                * Check Message Authentication *
                */
                String message = id + TOp + cmd + Ks;
                MessageDigest md = null;
            
                md = MessageDigest.getInstance("SHA-256");
                md.update(message.getBytes("UTF-8"));
                byte[] digest = md.digest();
                //Digest in Hexadecimal format
                StringBuilder myMac = new StringBuilder();
                for (byte b : digest) {
                    myMac.append(String.format("%02x", b));
                }
        
                //temp = OfficeDAO.aesEncode("" + myMac, Ks.substring(0, 16));
                temp[i] = "" + myMac;
            
                conn.close();
            
            }
            
            /**
             * Connect to SteelDB
             */
            conn = Conexao.getConnection();
            stm = conn.createStatement();

            /*((BFTStatement) stm).userID = id;
            ((BFTStatement) stm).TOp = TOp;
            ((BFTStatement) stm).MACs = macs;*/
            
            String stringMacs = "";
            for (int i = 0; i < macs.length; i++) {
                stringMacs = stringMacs + ";" + macs[i];
            }
            System.out.println("MACs: " + stringMacs);
            
            //try {
                
                stm.executeQuery("[TRIA];" + id + ";" + TOp + ";" + cmd + ";" + macs.length + stringMacs);
                
            /**} catch (TRIAReply rep) {
                
                ResultSet rs = (ResultSet) rep.getM().getContents();
                //get number of rows
                int rows = 0;
                if (rs.last()) {
                    rows = rs.getRow();
                    // Move to beginning
                    rs.beforeFirst();
                }
            
                System.out.println("Number of rows in the result set: " + rows);
                System.out.println("[WEBSERVICE]TRIA test: " + rep.getM().newTs + " // " + rep.getM().sigs);

            }*/
            
            conn.close();
            
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(SteelDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            java.util.logging.Logger.getLogger(SteelDB.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        System.out.println("Working: " + id + " // " + TOp + " // " + cmd);
        System.out.println("MACS: ");
        for (int i = 0; i < temp.length; i++) System.out.println(i + " : " + temp[i] + " // " + macs[i].equals(temp[i]));
        jsonResponse.put("Status", "NOK");
        jsonResponse.put("ID", id);
        jsonResponse.put("TOp", TOp);
        jsonResponse.put("RS", "");
        jsonResponse.put("EKsT", "");
        jsonResponse.put("signature", "");
            
        /////////////////////// TEMPORARIO ///////////////////////////
           
        PrintWriter out = response.getWriter();
        out.println(jsonResponse.toString());

        Logger.log("RESPONSE", "STEELDB");
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        doGet(request, response);
    }
 

}
