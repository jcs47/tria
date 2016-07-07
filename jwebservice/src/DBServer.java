
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.SQLException;

public class DBServer {

    /**
     * @param args
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    public static void main(String[] args) throws RemoteException, AlreadyBoundException, SQLException {

        /*Connection conn = Conexao.getConnection();
        System.out.println("Status: " + Conexao.status + "\n");
        conn.close();*/
        
        // Used to deploy the database RMI interface
        for (int i = 0; i < Authenticator.N; i++) {
        
            Registry reg = LocateRegistry.createRegistry(1099 + i);
            Authenticator auth = new Authenticator(i);
            reg.bind("authenticatorProxyDB" + i, auth);
        
        }
        System.out.println("Authenticator is up");
        
        
        /*OfficeDAO ofDao = new OfficeDAO();
        reg.bind("officeProxyDB", ofDao);
        System.out.println("Office is up\n");
               
        //Gestor g = new Gestor();
        //reg.bind("gestor", g);

        System.out.println("RMI is running");*/
    }

}
