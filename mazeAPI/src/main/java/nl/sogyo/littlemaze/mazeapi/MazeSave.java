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

@Path("save/{slot}")
public class MazeSave {
	
	@PUT
	public Response initialize(
			@PathParam("slot") String saveSlot,
			@Context HttpServletRequest request,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String token
			) {
		
		HttpSession session = request.getSession(false);
		MazeDto gameState = new MazeDto((Grid) session.getAttribute("mazegrid"), userName); 
		
		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				token.equals(session.getAttribute("token"))) {
			SqlConnect dbConnect = new SqlConnect("jdbc:mysql://localhost:2220/maze_safe");
			
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
