
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConnections {

    private static String id = "alice";
    private static String Sa = "2037794458";
    //private static String h = "ea3f39bd56bea8b3babd1b6012ebeec79534458dee71261d871581ac5c380d75";
    private static String h = "19ae057c77b7e568bbab63904ced34cbdea150b23c95bfa472f8660ab869e8ed";
                
    /**
     * @param args
     * @throws NotBoundException
     * @throws RemoteException
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException {

        //Test RMI
           
        System.out.println("######## Testing RMI connections\n");
        //Registry reg = LocateRegistry.getRegistry("169.254.83.95", 1099);
        Registry reg = LocateRegistry.getRegistry("127.0.0.1", 1099);
        

        //Mensageiro m = (Mensageiro) reg.lookup("gestor");
        //System.out.println(m.hello("Snake"));

        System.out.println("#### Testing (RMI) authentication");
        
        AuthenticatorInterface auth = (AuthenticatorInterface) reg.lookup("authenticatorProxyDB");
        
        MessageAuthentication m = null;
        System.out.println("Sending credentials for Alice");
        m = auth.doLogin(id, Sa, h);
        System.out.println("Status: " + m.getStatus() + "\n");
        
        System.out.println("#### Testing (RMI) Office");
        
        OfficeDAOInterface ofDao = (OfficeDAOInterface) reg.lookup("officeProxyDB");
        System.out.println(ofDao.hello("Snake") + "\n");
        
        MessageResponse mr = ofDao.getAllOfficeBase();
        
        for (Object o : mr.getRS()) {
            
            Office off = (Office) o;
            System.out.println(off + "\n");
        }
        

    }

}
