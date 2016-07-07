import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Server {

	/**
	 * @param args
	 * @throws RemoteException 
	 * @throws AlreadyBoundException 
	 */
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		Registry reg = LocateRegistry.createRegistry(1099);
		Gestor g = new Gestor();
		reg.bind("gestor", g);
		System.out.println("RMI is running");

	}

}
