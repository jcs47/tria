
public class User {
	
	private String id;
	private String secret;
	private String Ks;
	private String token;
	
	public User() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getKs() {
		return Ks;
	}

	public void setKs(String ks) {
		Ks = ks;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
