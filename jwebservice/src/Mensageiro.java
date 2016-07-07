import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Mensageiro extends Remote {

	public String hello(String nome) throws RemoteException;
}
