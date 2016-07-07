import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Logger {

	public static void log(String type, String op) {
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
		Date date = new Date();
		System.out.println(type + " to Operation " + op + " in " + dateFormat.format(date));
	}
}
