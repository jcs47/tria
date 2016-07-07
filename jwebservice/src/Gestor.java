import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Gestor extends UnicastRemoteObject implements Mensageiro {

	protected Gestor() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	public String hello(String nome) throws RemoteException {
		return "Al™ " + nome;
	}
	
	

}
