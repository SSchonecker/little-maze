package nl.sogyo.littlemaze.mazeapi.dtostructures;

import org.mindrot.jbcrypt.BCrypt;

public class UserInput {
	
	private String userName;
	private String password;
	private boolean createAccount;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setCreateAccount(boolean createAccount) {
		this.createAccount = createAccount;
	}

	public String getUserName() {
		return userName;
	}

	public String getPasswordHash() {
		return BCrypt.hashpw(password, BCrypt.gensalt(12));
	}
	
	public boolean getCreateAccount() {
		return createAccount;
	}
	
	public boolean checkPassword(String savedPasswordHash) {
		return BCrypt.checkpw(password, savedPasswordHash);
	}

}
