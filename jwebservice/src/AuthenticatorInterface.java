import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;


public interface AuthenticatorInterface extends Remote  {

	public MessageAuthentication doLogin(String id, String Sa, String h) throws RemoteException, SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException;
	
	public MessageAuthentication doLoginBase(String id, String p) throws RemoteException, SQLException;
	
}
