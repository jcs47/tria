
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
//import org.bouncycastle.asn1.x509.
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemReader;

import edu.uiuc.ncsa.security.util.pkcs.KeyUtil;
import org.json.*;


import sun.misc.BASE64Encoder;

public class OfficeDAO {

	private static Connection conn = null;
	private static Statement stm = null;
	private static String DEFAULT = "NULL";
	public static final String PRIVATE_KEY_FILE = "/Users/barretto/Documents/Keys/private_key_1024_pkcs8.pem";
	private static Random random = new Random();
	
	public OfficeDAO() throws SQLException {
		//conn = Conexao.getConnection();
		//stm = conn.createStatement();
	}
	
	
	public static String sign(String text) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, IOException, InvalidKeySpecException {
		
		//The Message
		//TODO DEBUG
		//System.out.println("Message: " + text);
		
		FileReader fr = null;
		try {
			fr = new FileReader(PRIVATE_KEY_FILE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrivateKey privateKey = null;
		try {
			privateKey = KeyUtil.fromPKCS8PEM(fr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] message = null;
		try {
			message = text.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Signature sig = null;
		try {
			sig = Signature.getInstance("SHA1WithRSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sig.initSign(privateKey);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			sig.update(message);
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] signatureByte = null;
		try {
			signatureByte = sig.sign();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Signature in Hexadecimal format
		StringBuilder sb = new StringBuilder();
		for(byte b : signatureByte) {
			sb.append(String.format("%02x", b));
		}
		
		//TODO DEBUG
		//System.out.println("Message: " + text);
		//System.out.println("Signature: " + sb.toString());
		fr.close();
		return sb.toString();
	}
	
	public static String aesEncode(String input, String key) {
		String ciphertext = "";
		String iv = "1234567890123456";
		
		/**
		// Print input
		for(int i = 0; i < input.length(); i++) {
			System.out.print((int)input.charAt(i) + " ");
		}
		System.out.println();
		
		// Print key
		for(int i = 0; i < key.length(); i++) {
			System.out.print((int)key.charAt(i) + " ");
		}
		System.out.println();
		
		// Print iv
		for(int i = 0; i < iv.length(); i++) {
			System.out.print((int)iv.charAt(i) + " ");
		}
		System.out.println();
		
		// Print key
		/*int[] keyDecimal = String64Encoded2intArray(key);
		for(int i = 0; i < keyDecimal.length; i++) {
			System.out.print(keyDecimal[i] + " ");
		}
		System.out.println();
		
		// Print iv
		int[] ivDecimal = String64Encoded2intArray(iv);
		for(int i = 0; i < ivDecimal.length; i++) {
			System.out.print(ivDecimal[i] + " ");
		}
		System.out.println();*/
		
		//Encrypt with EAS
		byte[] crypted = null;
		try {
			SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, skey, ips);
			byte[] ptext = input.getBytes();
			crypted = cipher.doFinal(ptext);
		} catch(Exception e) {
			System.out.println(e.toString());
		}
		
		ciphertext = new String(Hex.encodeHexString(crypted));

		//System.out.println("Message: " + input);
		//System.out.println("Cypher: " + ciphertext);
		return ciphertext;
	}
	
	public static String macSha256(String message) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(message.getBytes("UTF-8"));
		byte[] digest = md.digest();
		//Digest in Hexadecimal format
		StringBuilder myMac = new StringBuilder();
		for(byte b : digest) {
			myMac.append(String.format("%02x", b));
		}
		return myMac.toString();
	}
	
	public static MessageResponse getAllOffice(String id, String TOp, String hmac) throws SQLException, JSONException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeySpecException {
		
		MessageResponse mr = new MessageResponse();
		
		/** Connect to Database **/
		conn = Conexao.getConnection();
		stm = conn.createStatement();
		
		/** Get Session Key and Token **/
		String sqlToken = "SELECT Ks, token FROM users WHERE id = '" + id + "'";
		ResultSet rsToken = stm.executeQuery(sqlToken);
		String Ks = null;
		long token = 0;
		if(rsToken.next()) {
			Ks = rsToken.getString("Ks");
			token = rsToken.getLong("token");
		}
		
		/** Check Message Authentication **/
		String message = id + TOp + Ks;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(message.getBytes("UTF-8"));
		byte[] digest = md.digest();
		//Digest in Hexadecimal format
		StringBuilder myMac = new StringBuilder();
		for(byte b : digest) {
			myMac.append(String.format("%02x", b));
		}
		//TODO DEBUG
		//System.out.println("MyMAC = " + myMac);
		//System.out.println("MAC = " + hmac);
		if(myMac.toString().equals(hmac)) {
			/** Check the Token **/
			//if(Long.parseLong(TOp) == (token + 1)) {
			if(Long.parseLong(TOp) == (token)) {
				/** Set the new Token **/
				int T = (int)(1 + (Math.random() * 1E10));
				//long T = random.nextLong();
				String TString = "" + T;
				/*String sql = "UPDATE users SET  token = ? WHERE id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setInt(1, T);
				ps.setString(2, id);
				//ps.executeUpdate();*/
				
				/** Encode the New Token with XOR **/
				//TODO DEBUG
				//System.out.println("T: " + TString);
				//String EKsT = Authenticator.xorEncode(TString, Ks);
				
				/** Encode the New Token with AES **/
				//String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 32));
				String EKsT = OfficeDAO.aesEncode(TOp, Ks.substring(0, 32));
				
				/** Get the data **/
				String sqlOffice = "SELECT * from offices";
				
				ResultSet rs = stm.executeQuery(sqlOffice);
				ArrayList<Office>  offices = new ArrayList<Office>();
				offices.clear();
				String RStoString = "";
				while(rs.next()) {
					Office o = new Office();
					o.setOfficeCode(rs.getString("officeCode"));
					o.setCity(rs.getString("city"));
					o.setPhone(rs.getString("phone"));
					o.setAddressLine1(rs.getString("addressLine1"));
					o.setAddressLine2(rs.getString("addressLine2"));
					o.setState(rs.getString("state"));
					o.setCountry(rs.getString("country"));
					o.setPostalCode(rs.getString("postalCode"));
					o.setTerritory(rs.getString("territory"));
					RStoString += o.toString();
					offices.add(o);
				}
				//TODO DEBUG
				//System.out.println(RStoString);
				/** Close connection to Database **/
				rs.close();
				stm.close();
				conn.close();
				
				String status = "OK";
				
				/** Sign **/
				String messageToSign = status + id + TOp + RStoString + EKsT;
				String signature = sign(messageToSign);
				
				/** The Response **/
				mr.setStatus(status);
				mr.setId(id);
				mr.setTOp(TOp);
				mr.setRS(offices);
				mr.setEKsT(EKsT);
				mr.setSignature(signature);
			}
			else {
				String status = "NOK;Token Invalid";
				String messageToSign = status + id + TOp;
				String signature = sign(messageToSign);
				mr.setStatus(status);
				mr.setId(id);
				mr.setTOp(TOp);
				mr.setSignature(signature);
			}
		}
		else {
			String status = "NOK;MAC Invalid";
			String messageToSign = status + id + TOp;
			String signature = sign(messageToSign);
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setSignature(signature);
		}
		
		return mr;
	}
	
public static MessageResponse getAllOfficeBase() throws SQLException, JSONException {
		
		MessageResponse mr = new MessageResponse();
		
		/** Connect to Database **/
		conn = Conexao.getConnection();
		stm = conn.createStatement();
		/** Get the data **/
		String sqlOffice = "SELECT * from offices";

		ResultSet rs = stm.executeQuery(sqlOffice);
		ArrayList<Office>  offices = new ArrayList<Office>();
		offices.clear();
		while(rs.next()) {
			Office o = new Office();
			o.setOfficeCode(rs.getString("officeCode"));
			o.setCity(rs.getString("city"));
			o.setPhone(rs.getString("phone"));
			o.setAddressLine1(rs.getString("addressLine1"));
			o.setAddressLine2(rs.getString("addressLine2"));
			o.setState(rs.getString("state"));
			o.setCountry(rs.getString("country"));
			o.setPostalCode(rs.getString("postalCode"));
			o.setTerritory(rs.getString("territory"));
			offices.add(o);
		}
		//TODO DEBUG
		//System.out.println(RStoString);
		/** Close connection to Database **/
		rs.close();
		stm.close();
		conn.close();

		mr.setStatus("OK");
		mr.setRS(offices);
		return mr;
	}
	
	public static MessageResponse getOfficeByCode(String id, String TOp, String officeId, String mac) throws SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
		
		MessageResponse mr = new MessageResponse();
		
		/** Connect to Database **/
		conn = Conexao.getConnection();
		stm = conn.createStatement();
		
		/** Get Session Key and Token **/
		String sqlToken = "SELECT Ks, token FROM users WHERE id = '" + id + "'";
		ResultSet rsToken = stm.executeQuery(sqlToken);
		String Ks = null;
		long token = 0;
		if(rsToken.next()) {
			Ks = rsToken.getString("Ks");
			token = rsToken.getLong("token");
		}
		
		/** Check Message Authentication **/
		String message = id + TOp + officeId + Ks;
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(message.getBytes("UTF-8"));
		byte[] digest = md.digest();
		//Digest in Hexadecimal format
		StringBuilder myMac = new StringBuilder();
		for(byte b : digest) {
			myMac.append(String.format("%02x", b));
		}
		//TODO DEBUG
		//System.out.println("MyMAC = " + myMac);
		//System.out.println("MAC = " + hmac);
		if(myMac.toString().equals(mac)) {
			/** Check the Token **/
			if(Long.parseLong(TOp) == (token + 1)) {
				/** Set the new Token **/
				long T = (long)(1 + (Math.random() * 1E10));
				String TString = "" + T;
				String sql = "UPDATE users SET  token = ? WHERE id = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setLong(1, T);
				ps.setString(2, id);
				ps.executeUpdate();
				
				/** Encode the New Token with XOR **/
				//TODO DEBUG
				//System.out.println("T: " + TString);
				//String EKsT = Authenticator.xorEncode(TString, Ks);
				String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 32));
				
				/** Get the data **/
				sql = "SELECT * from offices WHERE officeCode = '" + officeId + "'";
				ArrayList<Office>  offices = new ArrayList<Office>();
				Office o = new Office();
				try {
					ResultSet rs = stm.executeQuery(sql);
					if(rs.next()) {
						o.setOfficeCode(rs.getString("officeCode"));
						//System.out.println("Outro teste: " + rs.getString(1));
						o.setCity(rs.getString("city"));
						o.setPhone(rs.getString("phone"));
						o.setAddressLine1(rs.getString("addressLine1"));
						o.setAddressLine2(rs.getString("addressLine2"));
						o.setState(rs.getString("state"));
						o.setCountry(rs.getString("country"));
						o.setPostalCode(rs.getString("postalCode"));
						o.setTerritory(rs.getString("territory"));
						offices.add(o);
					}
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				}
				
				/** Close connection to Database **/
				stm.close();
				conn.close();
				
				String status = "OK";
				
				/** Sign **/
				String messageToSign = status + id + TOp + o.toString() + EKsT;
				String signature = sign(messageToSign);
				
				/** The Response **/
				mr.setStatus(status);
				mr.setId(id);
				mr.setTOp(TOp);
				mr.setRS(offices);
				mr.setEKsT(EKsT);
				mr.setSignature(signature);
				
			}
			else {
				String status = "NOK;Token Invalid";
				String messageToSign = status + id + TOp;
				String signature = sign(messageToSign);
				mr.setStatus(status);
				mr.setId(id);
				mr.setTOp(TOp);
				mr.setSignature(signature);
			}
		}
		else {
			String status = "NOK;MAC Invalid";
			String messageToSign = status + id + TOp;
			String signature = sign(messageToSign);
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setSignature(signature);
		}
		
		return mr;
	}
	
public static MessageResponse getOfficeByCodeBase(String officeId) throws SQLException {
		
	MessageResponse mr = new MessageResponse();

	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();

	/** Get the data **/
	String sql = "SELECT * from offices WHERE officeCode = '" + officeId + "'";
	ArrayList<Office>  offices = new ArrayList<Office>();
	Office o = new Office();
	try {
		ResultSet rs = stm.executeQuery(sql);
		if(rs.next()) {
			o.setOfficeCode(rs.getString("officeCode"));
			o.setCity(rs.getString("city"));
			o.setPhone(rs.getString("phone"));
			o.setAddressLine1(rs.getString("addressLine1"));
			o.setAddressLine2(rs.getString("addressLine2"));
			o.setState(rs.getString("state"));
			o.setCountry(rs.getString("country"));
			o.setPostalCode(rs.getString("postalCode"));
			o.setTerritory(rs.getString("territory"));
			offices.add(o);
		}
	} catch (SQLException e) {
		System.out.println(e.getMessage());
	}

	/** Close connection to Database **/
	stm.close();
	conn.close();

	String status = "OK";

	/** The Response **/
	mr.setStatus(status);
	mr.setRS(offices);

	return mr;
	}


public static MessageResponse setOffice(String id, String TOp, String officeJsonString, String mac) throws SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
	
	MessageResponse mr = new MessageResponse();

	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();

	/** Get Session Key and Token **/
	String sqlToken = "SELECT Ks, token FROM users WHERE id = '" + id + "'";
	ResultSet rsToken = stm.executeQuery(sqlToken);
	String Ks = null;
	long token = 0;
	if(rsToken.next()) {
		Ks = rsToken.getString("Ks");
		token = rsToken.getLong("token");
	}

	/** Check Message Authentication **/
	String message = id + TOp + officeJsonString + Ks;
	MessageDigest md = MessageDigest.getInstance("SHA-256");
	md.update(message.getBytes("UTF-8"));
	byte[] digest = md.digest();
	//Digest in Hexadecimal format
	StringBuilder myMac = new StringBuilder();
	for(byte b : digest) {
		myMac.append(String.format("%02x", b));
	}
	//TODO DEBUG
	//System.out.println("MyMAC = " + myMac);
	//System.out.println("MAC = " + hmac);
	if(myMac.toString().equals(mac)) {
		/** Check the Token **/
		if(Long.parseLong(TOp) == (token + 1)) {
			/** Set the new Token **/
			long T = (long)(1 + (Math.random() * 1E10));
			String TString = "" + T;
			String sql = "UPDATE users SET  token = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, T);
			ps.setString(2, id);
			ps.executeUpdate();

			/** Encode the New Token with XOR **/
			//TODO DEBUG
			//System.out.println("T: " + TString);
			//String EKsT = Authenticator.xorEncode(TString, Ks);
			String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 32));

			/** Set the data **/
			JSONObject officeJson = new JSONObject(officeJsonString);
			sql = "UPDATE offices SET city = ?, phone = ?, addressLine1 = ?, addressLine2 = ?, ";
			sql += "state = ?, country = ?, postalCode = ?, territory = ? ";
			sql += "WHERE officeCode = ?";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, officeJson.getString("city"));
			ps2.setString(2, officeJson.getString("phone"));
			ps2.setString(3, officeJson.getString("addressLine1"));
			ps2.setString(4, officeJson.getString("addressLine2"));
			ps2.setString(5, officeJson.getString("state"));
			ps2.setString(6, officeJson.getString("country"));
			ps2.setString(7, officeJson.getString("postalCode"));
			ps2.setString(8, officeJson.getString("territory"));
			ps2.setString(9, officeJson.getString("officeCode"));
			ps2.executeUpdate();

			ArrayList rs = new ArrayList<Office>();
			rs.add("OK");

			/** Close connection to Database **/
			stm.close();
			conn.close();

			String status = "OK";
			
			/** Sign **/
			String messageToSign = status + id + TOp + "OK" + EKsT;
			String signature = sign(messageToSign);
			
			/** The Response **/
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setRS(rs);
			mr.setEKsT(EKsT);
			mr.setSignature(signature);	
		}
		else {
			String status = "NOK;Token Invalid";
			String messageToSign = status + id + TOp;
			String signature = sign(messageToSign);
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setSignature(signature);
		}
	}
	else {
		String status = "NOK;MAC Invalid";
		String messageToSign = status + id + TOp;
		String signature = sign(messageToSign);
		mr.setStatus(status);
		mr.setId(id);
		mr.setTOp(TOp);
		mr.setSignature(signature);
	}

	return mr;
	}

public static MessageResponse setOfficeBase(String officeJsonString) throws SQLException {
	
	MessageResponse mr = new MessageResponse();

	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();

	/** Set the data **/
	JSONObject officeJson = new JSONObject(officeJsonString);
	String sql = "UPDATE offices SET city = ?, phone = ?, addressLine1 = ?, addressLine2 = ?, ";
	sql += "state = ?, country = ?, postalCode = ?, territory = ? ";
	sql += "WHERE officeCode = ?";
	PreparedStatement ps2 = conn.prepareStatement(sql);
	ps2.setString(1, officeJson.getString("city"));
	ps2.setString(2, officeJson.getString("phone"));
	ps2.setString(3, officeJson.getString("addressLine1"));
	ps2.setString(4, officeJson.getString("addressLine2"));
	ps2.setString(5, officeJson.getString("state"));
	ps2.setString(6, officeJson.getString("country"));
	ps2.setString(7, officeJson.getString("postalCode"));
	ps2.setString(8, officeJson.getString("territory"));
	ps2.setString(9, officeJson.getString("officeCode"));
	ps2.executeUpdate();

	ArrayList rs = new ArrayList<Office>();
	rs.add("OK");

	/** Close connection to Database **/
	stm.close();
	conn.close();

	String status = "OK";

	/** The Response **/
	mr.setStatus(status);
	mr.setRS(rs);

	return mr;
}

public static MessageResponse insertOffice(String id, String TOp, String officeJsonString, String mac) throws SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
	MessageResponse mr = new MessageResponse();

	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();

	/** Get Session Key and Token **/
	String sqlToken = "SELECT Ks, token FROM users WHERE id = '" + id + "'";
	ResultSet rsToken = stm.executeQuery(sqlToken);
	String Ks = null;
	long token = 0;
	if(rsToken.next()) {
		Ks = rsToken.getString("Ks");
		token = rsToken.getLong("token");
	}

	/** Check Message Authentication **/
	String message = id + TOp + officeJsonString + Ks;
	MessageDigest md = MessageDigest.getInstance("SHA-256");
	md.update(message.getBytes("UTF-8"));
	byte[] digest = md.digest();
	//Digest in Hexadecimal format
	StringBuilder myMac = new StringBuilder();
	for(byte b : digest) {
		myMac.append(String.format("%02x", b));
	}
	//TODO DEBUG
	//System.out.println("MyMAC = " + myMac);
	//System.out.println("MAC = " + hmac);
	if(myMac.toString().equals(mac)) {
		/** Check the Token **/
		if(Long.parseLong(TOp) == (token + 1)) {
			/** Set the new Token **/
			long T = (long)(1 + (Math.random() * 1E10));
			String TString = "" + T;
			String sql = "UPDATE users SET  token = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, T);
			ps.setString(2, id);
			ps.executeUpdate();

			/** Encode the New Token with XOR **/
			//TODO DEBUG
			//System.out.println("T: " + TString);
			//String EKsT = Authenticator.xorEncode(TString, Ks);
			String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 32));

			/** Insert the data **/
			JSONObject officeJson = new JSONObject(officeJsonString);
			sql = "INSERT INTO offices VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, officeJson.getString("officeCode"));
			ps2.setString(2, officeJson.getString("city"));
			ps2.setString(3, officeJson.getString("phone"));
			ps2.setString(4, officeJson.getString("addressLine1"));
			ps2.setString(5, officeJson.getString("addressLine2"));
			ps2.setString(6, officeJson.getString("state"));
			ps2.setString(7, officeJson.getString("country"));
			ps2.setString(8, officeJson.getString("postalCode"));
			ps2.setString(9, officeJson.getString("territory"));
			ps2.executeUpdate();

			ArrayList rs = new ArrayList<Office>();
			rs.add("OK");

			/** Close connection to Database **/
			stm.close();
			conn.close();
			
			String status = "OK";
			
			/** Sign **/
			String messageToSign = status + id + TOp + "OK" + EKsT;
			String signature = sign(messageToSign);
			
			/** The Response **/
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setRS(rs);
			mr.setEKsT(EKsT);
			mr.setSignature(signature);	
		}
		else {
			String status = "NOK;Token Invalid";
			String messageToSign = status + id + TOp;
			String signature = sign(messageToSign);
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setSignature(signature);
		}
	}
	else {
		String status = "NOK;MAC Invalid";
		String messageToSign = status + id + TOp;
		String signature = sign(messageToSign);
		mr.setStatus(status);
		mr.setStatus(status);
		mr.setId(id);
		mr.setTOp(TOp);
		mr.setSignature(signature);
	}

	return mr;
}

public static MessageResponse insertOfficeBase(String officeJsonString) throws SQLException {
	MessageResponse mr = new MessageResponse();

	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();

	/** Insert the data **/
	JSONObject officeJson = new JSONObject(officeJsonString);
	String sql = "INSERT INTO offices VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement ps2 = conn.prepareStatement(sql);
	ps2.setString(1, officeJson.getString("officeCode"));
	ps2.setString(2, officeJson.getString("city"));
	ps2.setString(3, officeJson.getString("phone"));
	ps2.setString(4, officeJson.getString("addressLine1"));
	ps2.setString(5, officeJson.getString("addressLine2"));
	ps2.setString(6, officeJson.getString("state"));
	ps2.setString(7, officeJson.getString("country"));
	ps2.setString(8, officeJson.getString("postalCode"));
	ps2.setString(9, officeJson.getString("territory"));
	ps2.executeUpdate();

	ArrayList rs = new ArrayList<Office>();
	rs.add("OK");

	/** Close connection to Database **/
	stm.close();
	conn.close();

	String status = "OK";

	/** The Response **/
	mr.setStatus(status);
	mr.setRS(rs);

	return mr;
}

public static MessageResponse deleteOffice(String id, String TOp, String officeId, String mac) throws SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {
	MessageResponse mr = new MessageResponse();
	
	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();
	
	/** Get Session Key and Token **/
	String sqlToken = "SELECT Ks, token FROM users WHERE id = '" + id + "'";
	ResultSet rsToken = stm.executeQuery(sqlToken);
	String Ks = null;
	long token = 0;
	if(rsToken.next()) {
		Ks = rsToken.getString("Ks");
		token = rsToken.getLong("token");
	}
	
	/** Check Message Authentication **/
	String message = id + TOp + officeId + Ks;
	MessageDigest md = MessageDigest.getInstance("SHA-256");
	md.update(message.getBytes("UTF-8"));
	byte[] digest = md.digest();
	//Digest in Hexadecimal format
	StringBuilder myMac = new StringBuilder();
	for(byte b : digest) {
		myMac.append(String.format("%02x", b));
	}
	//TODO DEBUG
	//System.out.println("MyMAC = " + myMac);
	//System.out.println("MAC = " + hmac);
	if(myMac.toString().equals(mac)) {
		/** Check the Token **/
		if(Long.parseLong(TOp) == (token + 1)) {
			/** Set the new Token **/
			long T = (long)(1 + (Math.random() * 1E10));
			String TString = "" + T;
			String sql = "UPDATE users SET  token = ? WHERE id = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setLong(1, T);
			ps.setString(2, id);
			ps.executeUpdate();
			
			/** Encode the New Token with XOR **/
			//TODO DEBUG
			//System.out.println("T: " + TString);
			//String EKsT = Authenticator.xorEncode(TString, Ks);
			String EKsT = OfficeDAO.aesEncode(TString, Ks.substring(0, 32));
			
			/** Delete the data **/
			sql = "DELETE FROM offices WHERE officeCode = ? ";
			PreparedStatement ps2 = conn.prepareStatement(sql);
			ps2.setString(1, officeId);
			ps2.execute();
			
			ArrayList rs = new ArrayList<Office>();
			rs.add("OK");
			
			/** Close connection to Database **/
			stm.close();
			conn.close();
			
			String status = "OK";
			
			/** Sign **/
			String messageToSign = status + id + TOp + "OK" + EKsT;
			String signature = sign(messageToSign);
			
			/** The Response **/
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setRS(rs);
			mr.setEKsT(EKsT);
			mr.setSignature(signature);	
		}
		else {
			String status = "NOK;Token Invalid";
			String messageToSign = status + id + TOp;
			String signature = sign(messageToSign);
			mr.setStatus(status);
			mr.setId(id);
			mr.setTOp(TOp);
			mr.setSignature(signature);
		}
	}
	else {
		String status = "NOK;MAC Invalid";
		String messageToSign = status + id + TOp;
		String signature = sign(messageToSign);
		mr.setStatus(status);
		mr.setStatus(status);
		mr.setId(id);
		mr.setTOp(TOp);
		mr.setSignature(signature);
	}
	
	return mr;
}

public static MessageResponse deleteOfficeBase(String officeId) throws SQLException {
	MessageResponse mr = new MessageResponse();

	/** Connect to Database **/
	conn = Conexao.getConnection();
	stm = conn.createStatement();

	/** Delete the data **/
	String sql = "DELETE FROM offices WHERE officeCode = ? ";
	PreparedStatement ps2 = conn.prepareStatement(sql);
	ps2.setString(1, officeId);
	ps2.execute();

	ArrayList rs = new ArrayList<Office>();
	rs.add("OK");

	/** Close connection to Database **/
	stm.close();
	conn.close();

	String status = "OK";

	/** The Response **/
	mr.setStatus(status);
	mr.setRS(rs);

	return mr;
}
	
}

