import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

import org.json.JSONException;


public interface OfficeDAOInterface extends Remote {
	
	public MessageResponse getAllOffice(String id, String t, String h) throws SQLException, JSONException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException, InvalidKeySpecException, RemoteException;
	
	public MessageResponse getAllOfficeBase() throws RemoteException, SQLException, JSONException;
	
	public MessageResponse getOfficeByCode(String id, String t, String oid, String m)  throws RemoteException, SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException;
	
	public MessageResponse getOfficeByCodeBase(String oid)  throws RemoteException, SQLException;
	
	public MessageResponse setOffice(String id, String t, String ojson, String mac)  throws RemoteException, SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException, JSONException;
	
	public MessageResponse setOfficeBase(String ojson)  throws RemoteException, SQLException, JSONException;
	
	public MessageResponse insertOffice(String id, String t, String ojson, String m)  throws RemoteException, SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException, JSONException;
	
	public MessageResponse insertOfficeBase(String ojson)  throws RemoteException, SQLException, JSONException;
	
	public MessageResponse deleteOffice(String id, String t, String oid, String m)  throws RemoteException, SQLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, InvalidKeySpecException, IOException;
	
	public MessageResponse deleteOfficeBase(String oid)  throws RemoteException, SQLException;
        
        public String hello(String nome) throws RemoteException;
	
}
