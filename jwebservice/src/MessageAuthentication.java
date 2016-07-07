import java.io.Serializable;


public class MessageAuthentication implements Serializable {
		
		
		private static final long serialVersionUID = 1L;
		private String status;
		private String id;
		private String EKsT;
		private String Sa;
		private String Sb;
		private String sign;
		
		public MessageAuthentication() {
			
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getEKsT() {
			return EKsT;
		}

		public void setEKsT(String eKsT) {
			EKsT = eKsT;
		}

		public String getSa() {
			return Sa;
		}

		public void setSa(String sa) {
			Sa = sa;
		}

		public String getSb() {
			return Sb;
		}

		public void setSb(String sb) {
			Sb = sb;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
