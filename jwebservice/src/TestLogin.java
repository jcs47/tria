import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;


public class TestLogin {

	public static void main(String[] args) throws IOException, NotBoundException {
		
		String id = "alice";
		String Sa = "2037794458";
		String h = "ea3f39bd56bea8b3babd1b6012ebeec79534458dee71261d871581ac5c380d75";
		
		long startTime;
		long endTime;
		int requests = 2;
		long[] time = new long[requests];
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		MessageAuthentication m = null;
		try {
			for (int i = 0; i < requests; i++) {
				startTime = System.currentTimeMillis();
				Registry reg = LocateRegistry.getRegistry("169.254.83.95", 1099);
				AuthenticatorInterface auth = (AuthenticatorInterface) reg.lookup("authenticatorProxyDB");
				m = auth.doLogin(id, Sa, h);
				endTime = System.currentTimeMillis();
				//if (i > 1000) {
					stats.addValue(endTime - startTime);
					System.out.println(i + ";" + m.getStatus() + ";" + (endTime - startTime));
				//}
			}
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Compute some statistics
		double mean = stats.getMean();
		double std = stats.getStandardDeviation();
		System.out.println(mean + ";" + std);
	}

}
