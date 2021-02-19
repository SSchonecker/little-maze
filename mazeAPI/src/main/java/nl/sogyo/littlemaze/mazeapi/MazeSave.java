package nl.sogyo.littlemaze.mazeapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import nl.sogyo.littlemaze.Grid;
import nl.sogyo.littlemaze.mazeapi.dbconnect.SqlConnect;
import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;

/**
 * Class handling save requests.
 * 
 * It listens to PUT requests to /save/{slot}
 * where {slot} is the slot into which to save the session's grid.
 * Only works if the userName and access token from the header correspond to the session's.
 * 
 */
@Path("save/{slot}")
public class MazeSave {
	
	@PUT
	public Response initialize(
			@PathParam("slot") String saveSlot,
			@Context HttpServletRequest request,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String accessToken
			) {
		
		HttpSession session = request.getSession(false);
		MazeDto gameState = new MazeDto((Grid) session.getAttribute("mazegrid"), userName); 
		
		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				accessToken.equals(session.getAttribute("accessToken"))) {
			SqlConnect dbConnect = new SqlConnect("jdbc:mysql://localhost:4444/maze_safe");
			try {
				dbConnect.saveGame(gameState, userName, saveSlot);
				responseStatus = 200;
				return Response.status(responseStatus).build();
			}
			catch (Exception err) {
				responseStatus = 406;
				return Response.status(responseStatus).entity(err.getMessage()).build();
			}
		}
		return Response.status(responseStatus).build();
	}
}
