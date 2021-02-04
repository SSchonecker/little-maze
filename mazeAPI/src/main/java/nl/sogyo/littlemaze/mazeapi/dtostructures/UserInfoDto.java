package nl.sogyo.littlemaze.mazeapi.dtostructures;

public class UserInfoDto {
	
	private String userName;
	private String token;
	
	public UserInfoDto(String name, String accessToken) {
		userName = name;
		token = accessToken;
	}
	
	public String getUserName() { return userName; }
	
	public String getToken() { return token; }

}
