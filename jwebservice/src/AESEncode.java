import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

import sun.misc.BASE64Decoder;


public class AESEncode {
	public AESEncode() {}
	
	public static String encrypt(String input, String key) throws IOException {
		
		String ciphertext = "";
		String iv = "1234567890123456";
		
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
		
		System.out.println(new String(Hex.encodeHexString(crypted)));

		return ciphertext;
	}
	
	public static int[] String64Encoded2intArray(String text64Encoded) throws IOException
	{
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] barray = decoder.decodeBuffer(text64Encoded);
		
		int[] iarray = new int[barray.length];
		int i = 0;
		for (byte b : barray)
			iarray[i++] = b & 0xff;
		// "and" with 0xff since bytes are signed in java
		return iarray;
	}
	
	public static void main(String[] args) throws IOException {
		String input = "123456789012345";
		String key = "de1310982b646af063e7314e8ddd4787";
		
		AESEncode.encrypt(input, key);
	}
}
