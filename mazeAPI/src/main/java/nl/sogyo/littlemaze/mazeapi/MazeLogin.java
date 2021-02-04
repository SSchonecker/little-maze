package nl.sogyo.littlemaze.mazeapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.sogyo.littlemaze.mazeapi.dtostructures.UserInfoDto;
import nl.sogyo.littlemaze.mazeapi.dtostructures.UserInput;

@Path("login")
public class MazeLogin {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request, 
			UserInput loginInfo) {
		
		HttpSession session = request.getSession(true);
		
		String userName = loginInfo.getUserName();
		boolean createAccount = loginInfo.getCreateAccount();
		
		//TODO check if password in DB by getting DB.passwordHash and doing loginInfo.checkPassword(savedPasswordHash)
		if (createAccount) { String password = loginInfo.getPasswordHash(); 
			//TODO and set in DB
		}
		
		String accessToken = "hello token";
		
		session.setAttribute("userName", userName);
		session.setAttribute("token", accessToken);

		var output = new UserInfoDto(userName, accessToken);
		return Response.status(200).entity(output).build();
	}
}