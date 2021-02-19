package nl.sogyo.littlemaze.mazeapi;

import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.sogyo.littlemaze.mazeapi.dbconnect.DataRow;
import nl.sogyo.littlemaze.mazeapi.dbconnect.SqlConnect;
import nl.sogyo.littlemaze.mazeapi.dtostructures.JsonError;
import nl.sogyo.littlemaze.mazeapi.dtostructures.UserInfoDto;
import nl.sogyo.littlemaze.mazeapi.dtostructures.UserInput;

/**
 * Class handling login requests.
 * 
 * It listens to POST requests to /login
 * and expects a json containing userName, password and createAccount fields.
 * The latter is a boolean indicating whether a new account should be created.
 * 
 * The OK response gives a json with userName, accessToken and saveSlotUsed fields.
 * In case of an error, it is forwarded through a json error object, containing the error message.
 */
@Path("login")
public class MazeLogin {
	
	// Fields for the generation of the access token
	private static final SecureRandom secureRandom = new SecureRandom();
	private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request, 
			UserInput loginInfo) {
		
		HttpSession session = request.getSession(true);
		String error = "";
		
		String userName = loginInfo.getUserName();
		boolean createAccount = loginInfo.getCreateAccount();
		String password = loginInfo.getPasswordHash();
		
		SqlConnect dbConnect = new SqlConnect("jdbc:mysql://localhost:4444/maze_safe");
		int saveSlotUsed = 0;
		
		if (createAccount) {
			try {
				dbConnect.addUser(userName, password);
			} catch (SQLException exc) {
				error = exc.getMessage();
			}
		}
		else {
			try {
				DataRow data = dbConnect.getUserInfo(userName);
				if (!loginInfo.checkPassword(data.getPassword())) {
					error = "Invalid password";
				}
				else {
					saveSlotUsed = data.slotsUsed();
				}
			} catch (SQLException exc) {
				error = exc.getMessage();
			}
		}
		
		if (error.equals("")) {
			String accessToken = generateNewToken();
			
			session.setAttribute("userName", userName);
			session.setAttribute("accessToken", accessToken);

			var output = new UserInfoDto(userName, accessToken, saveSlotUsed);
			return Response.status(200).entity(output).build();
		}
		else {
			var output = new JsonError(error);
			return Response.status(406).entity(output).build();
		}
	}
	
	/**
	 * Method to generate a random access token
	 * @return String the token
	 */
	public static String generateNewToken() {
		byte[] randomBytes = new byte[24];
		secureRandom.nextBytes(randomBytes);
		return base64Encoder.encodeToString(randomBytes);
	}
}