import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.apache.commons.codec.binary.Hex;


public class Authenticator {
	
	private static Connection conn = null;
	private static Statement stm = null;
	
	public Authenticator() {
		
	}
	
	public static MessageAuthentication doLogin(String id, String Sa, String h) throws SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
		conn = Conexao.getConnection();
		stm = conn.createStatement();
		MessageAuthentication m = new MessageAuthentication();
		String sql = "SELECT * FROM users WHERE id = '" + id + "' ";
		ResultSet rs = stm.executeQuery(sql);
		if(rs.next()) {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			String message = rs.getString("secret") + Sa;
			//System.out.println("Message: " + message);
			md.update(message.getBytes("UTF-8"));
			byte[] digest = md.digest();
			//Digest in Hexadecimal format
			StringBuilder sb = new StringBuilder();
			for(byte b : digest) {
				sb.append(String.format("%02x", b));
			}
			//TODO DEBUG
			//System.out.println("h = " + sb.toString());
			
			if(sb.toString().equals(h)) {
				// The Token
				long T = (int)(1 + (Math.random() * 1E10));
				String TString = "" + T;
				
				// The Server Salt
				long Sb = (int)(1 + (Math.random() * 1E10));
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
				String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 32));
				
				
				// Sign The Message
				String response = "OK" + rs.getString("id") + EKsT + Sa + SbString;
				String signature = OfficeDAO.sign(response);
				
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
	
	public static MessageAuthentication doLoginBase(String id, String p) throws SQLException {
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
