package nl.sogyo.littlemaze.mazeapi.dtostructures;

public class UserInfoDto {
	
	private String userName;
	private String token;
	private int saveSlotUsed;
	
	public UserInfoDto(String name, String accessToken, int saveSlotUsed) {
		userName = name;
		token = accessToken;
		this.saveSlotUsed = saveSlotUsed;
	}
	
	public String getUserName() { return userName; }
	
	public String getToken() { return token; }
	
	public int getSaveSlotUsed() { return saveSlotUsed; }

}
