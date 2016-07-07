import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;

// This code renders the class an RMI remote method
public class Authenticator extends UnicastRemoteObject implements AuthenticatorInterface {
// This code renders the class just a regular object
//public class Authenticator {

	private static Connection conn = null;
	private static Statement stm = null;
	
        //temporario, proxy n pode conhecer isto!
        private static String[] P = {
            "e44ad42e94635f2b2fd4bc926fe82fc4f7879510",
            "6259dce4bfe9fffb50ee7fd3c9cb94edaa9856c5",
            "4e2db30be249a73f7c3babad4f763f4645efd498",
            "2d797c0956a873ab2dce1642bcc54e56a3c052ac" };
        
        public static int N = 4;
        public static int SEED = 3559;
        
        public final int id;
        
	public Authenticator(int id) throws RemoteException {
		super();
                this.id = id;
	}
	
	public MessageAuthentication doLogin(String id, String Sa, String h) throws SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
		
                // connection with replicated database
                //conn = Conexao.getConnection();
            
                //connection with single database
                conn = Conexao.getSingleConnection(this.id);
		stm = conn.createStatement();
		MessageAuthentication m = new MessageAuthentication();
		String sql = "SELECT * FROM users WHERE id = '" + id + "' ";
		ResultSet rs = stm.executeQuery(sql);
		if(rs.next()) {
                    
                        System.out.println("secret = " + rs.getString("secret"));
                        
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String message = rs.getString("secret") + Sa;
                        
                        System.out.println("message = " + message);
                        
			//System.out.println("Message: " + message);
			md.update(message.getBytes("UTF-8"));
			byte[] digest = md.digest();
			//Digest in Hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			//TODO DEBUG
                        System.out.println("sb = " + sb.toString());
                        System.out.println("h = " + h);
                        
                        //Temporario!
                        /*System.out.println("******** test **********");
                        
                        String[] messages = new String[P.length];
                        
                        for (int i = 0; i < P.length; i++) {
                            messages[i] = P[i] + Sa;
                            md.update(messages[i].getBytes("UTF-8"));
                            digest = md.digest();
                            //Digest in Hexadecimal format
                            StringBuilder t = new StringBuilder();
                            for(byte b : digest) {
                        	t.append(String.format("%02x", b));
                            }
                            //TODO DEBUG
                            System.out.println("[a]H" + i + " = " + t.toString());
                            //System.out.println("[b]H" + i + " = " + Hi[i]);

                        }
                        
                        System.out.println("******** test/**********");*/
                        
                        
			if(sb.toString().equals(h)) {
				// The Token
                                //int T = (int)(1 + (Math.random() * 1E10));
				long T = (int) (1 + ((new Random(SEED)).nextLong() * (long) 1E10));
				String TString = "" + T;
				
				// The Server Salt
				//long Sb = (int)(1 + (Math.random() * 1E10));
                                long Sb = (int) (1 + ((new SecureRandom()).nextLong() * (long) 1E10));
                                String SbString = "" + Sb;
				
				// The Session Key
				MessageDigest mdsha1 = MessageDigest.getInstance("SHA-1");
				String messageKs = Sa  + SbString + rs.getString("secret");
				mdsha1.update(messageKs.getBytes("UTF-8"));
				byte[] digestKs = mdsha1.digest();
				StringBuilder sb2 = new StringBuilder();
				for(byte b : digestKs) {
					sb2.append(String.format("%02x", b));
				}
				String Ks = sb2.toString();
				//TODO DEBUG
				//System.out.println("Ks = " + Ks);
				
				//Update the fields in the database
				sql = "UPDATE users SET Ks = ?, token = ? WHERE id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, Ks);
				ps.setLong(2, T);
				ps.setString(3, id);
				ps.executeUpdate();
				
				//Encoding the Token with XOR
				//TODO DEBUG
				//System.out.println("T: " + TString);
				//String EKsT = xorEncode(TString, Ks);
				
                                
                                String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 16));
				//String EKsT = "teste";
				
				// Sign The Message
				String response = "OK" + rs.getString("id") + EKsT + Sa + SbString;
				String signature = OfficeDAO.sign(response);
                                
                                System.out.println("OK SIGNIED");
				
                                System.out.println(TString);
                                System.out.println(T);
				System.out.println(EKsT);
				System.out.println(Sa);
				System.out.println(SbString);
				System.out.println(signature);
				
				// Setting the Response Message
				m.setStatus("OK");
				m.setId(rs.getString("id"));
				m.setEKsT(EKsT);
				m.setSa(Sa);
				m.setSb(SbString);
				m.setSign(signature);
			}
			/** Invalid ID/Password **/
			else {
				String response = "NOK" + id + "0" + Sa + "0";
				String signature = OfficeDAO.sign(response);
                                System.out.println("NOK SIGNIED");
				// Setting the Response Message
				m.setStatus("NOK");
				m.setId(id);
				m.setEKsT("0");
				m.setSa(Sa);
				m.setSb("0");
				m.setSign(signature);
			}
		}
		
		conn.close();
		return m;
	}
	
	public static String xorEncode(String text, String key) {
		String encode = "";
		
		for(int i = 0; i < text.length(); i++) {
			int c = (int)text.charAt(i) ^ (int)key.charAt(i % key.length());
			//System.out.println(("" + (int)text.charAt(i)) + "^" + ("" + (int)key.charAt(i % key.length())) + "=" + ("" + c));
			String ch = "" + c;
			if(ch.length() == 1) { ch = "00" + ch; }
			if(ch.length() == 2) { ch = "0" + ch; }
			encode += ch;
		}
		return encode;
	}
	
	public MessageAuthentication doLoginBase(String id, String p) throws SQLException {
		conn = Conexao.getConnection();
		stm = conn.createStatement();
		MessageAuthentication m = new MessageAuthentication();
		String sql = "SELECT * FROM users WHERE id = '" + id + "' and password = '" + p + "'";
		ResultSet rs = stm.executeQuery(sql);
		if(rs.next()) {
			
				// Setting the Response Message
				m.setStatus("OK");
			}
			/** Invalid ID/Password **/
			else {
				// Setting the Response Message
				m.setStatus("NOK");
			}
		conn.close();
		return m;
	}
}
