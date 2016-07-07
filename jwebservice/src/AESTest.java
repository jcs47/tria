import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class AESTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String text = "";
		int kb = 1024;
		int mb = 1024 * 1024;
		
		// Size of text
		for(int i = 0; i < 100*kb; i++) {
			text += "a";
		}
		
		String key = "a6082f0b24f31e3cb20b60029d57d5f2";
		long startTime;
		long endTime;
		int requests = 1;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		String m = "";
		try {
			for (int i = 0; i < requests; i++) {
				startTime = System.currentTimeMillis();
				m = OfficeDAO.aesEncode(text, key);
				endTime = System.currentTimeMillis();
				/*if (i > 1000) {
					stats.addValue(endTime - startTime);
					System.out.println(i + ";" + (endTime - startTime));
				}*/
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Compute some statistics
		/*double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		System.out.println(mean + ";" + std);*/
		//
		//System.out.println(text);
		System.out.println(m);

	}

}
