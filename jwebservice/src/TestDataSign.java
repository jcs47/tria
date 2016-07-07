import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.json.JSONException;


public class TestDataSign {

	public static void main(String[] args) throws IOException {
		
		String text = "";
		int kb = 1024;
		int mb = 1024 * 1024;
		
		// Size of text
		for(int i = 0; i < 100; i++) {
			text += "a";
		}
		
		long startTime;
		long endTime;
		int requests = 1;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		String m = "";
		try {
			for (int i = 0; i < requests; i++) {
				startTime = System.currentTimeMillis();
				m = OfficeDAO.sign(text);
				endTime = System.currentTimeMillis();
				/*if (i > 1000) {
					stats.addValue(endTime - startTime);
					System.out.println(i + ";" + (endTime - startTime));
				}*/
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (SignatureException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		
		// Compute some statistics
		/*double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		System.out.println(mean + ";" + std);*/
		//
		System.out.println(text);
		System.out.println(m);

	}

}
