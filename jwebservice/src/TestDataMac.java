import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class TestDataMac {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String text = "";
		int kb = 1024;
		int mb = 1024 * 1024;
		
		// Size of text
		for(int i = 0; i < mb; i++) {
			text += "a";
		}
		
		long startTime;
		long endTime;
		int requests = 11000;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		String m = "";
		try {
			for (int i = 0; i < requests; i++) {
				startTime = System.currentTimeMillis();
				m = OfficeDAO.macSha256(text);
				endTime = System.currentTimeMillis();
				if (i > 1000) {
					stats.addValue(endTime - startTime);
					System.out.println(i + ";" + (endTime - startTime));
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		// Compute some statistics
		double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		System.out.println(mean + ";" + std);

	}

}
