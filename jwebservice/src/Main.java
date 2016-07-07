import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Main {

	/**
	 * @param args
	 * @throws NotBoundException 
	 * @throws RemoteException 
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException {
		
		Registry reg = LocateRegistry.getRegistry("169.254.83.95", 1099);
		Mensageiro m = (Mensageiro) reg.lookup("gestor");
		
		System.out.println(m.hello("Anderson"));

	}

}
