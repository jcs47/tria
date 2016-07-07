import java.io.Serializable;
import java.util.ArrayList;


public class MessageResponse implements Serializable {
	private String status;
	private String id;
	private String TOp;
	private ArrayList RS;
	private String EKsT;
	private String EKsT2;
	private String signature;
	
	public MessageResponse() {}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTOp() {
		return TOp;
	}
	public void setTOp(String tOp) {
		TOp = tOp;
	}
	public ArrayList getRS() {
		return RS;
	}
	public void setRS(ArrayList rS) {
		RS = rS;
	}
	public String getEKsT() {
		return EKsT;
	}
	public void setEKsT(String eKsT) {
		EKsT = eKsT;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getEKsT2() {
		return EKsT2;
	}

	public void setEKsT2(String eKsT2) {
		EKsT2 = eKsT2;
	}
	
	
}
