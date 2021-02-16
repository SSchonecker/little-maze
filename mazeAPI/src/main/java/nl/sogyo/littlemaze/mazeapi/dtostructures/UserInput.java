package nl.sogyo.littlemaze.mazeapi.dtostructures;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Class for the login information sent by the FE.
 * Only returns the password in hashed form.
 *
 */
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
	
	/**
	 * Method to compare a password hash from the DB with
	 * the current password.
	 * @param savedPasswordHash
	 * @return whether the password is correct
	 */
	public boolean checkPassword(String savedPasswordHash) {
		return BCrypt.checkpw(password, savedPasswordHash);
	}

}
