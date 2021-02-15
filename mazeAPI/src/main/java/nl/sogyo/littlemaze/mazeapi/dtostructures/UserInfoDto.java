package nl.sogyo.littlemaze.mazeapi.dtostructures;

/**
 * Class containing the relevant information on a logged in user.
 * A json of an object of this type is sent to the FE.
 * Used in MazeLogin.
 *
 */
public class UserInfoDto {
	
	private String userName;
	private String accessToken;
	private int saveSlotUsed;
	
	public UserInfoDto(String name, String accessToken, int saveSlotUsed) {
		userName = name;
		this.accessToken = accessToken;
		this.saveSlotUsed = saveSlotUsed;
	}
	
	public String getUserName() { return userName; }
	
	public String getToken() { return accessToken; }
	
	public int getSaveSlotUsed() { return saveSlotUsed; }

}
