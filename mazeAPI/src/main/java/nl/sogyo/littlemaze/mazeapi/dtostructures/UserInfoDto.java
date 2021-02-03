package nl.sogyo.littlemaze.mazeapi.dtostructures;

public class UserInfoDto {
	
	private String userName;
	
	public UserInfoDto(String name) {
		userName = name;
	}
	
	public String getUserName() { return userName; }

}
