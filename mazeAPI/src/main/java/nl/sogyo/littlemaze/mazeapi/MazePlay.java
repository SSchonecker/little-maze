package nl.sogyo.littlemaze.mazeapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import nl.sogyo.littlemaze.Grid;
import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;

/**
 * Class handling player actions during the game.
 * 
 * It listens to PUT requests to /stir/{key} for moves based on the keyboard key provided
 * or PUT requests to /stir/tile/{position}, where {position} is expected to be
 * a two-character string containing the selected tile's coordinates.
 * 
 * Both only work if the correct userName and accessToken are passed in the header.
 * Both update the session's grid object, and respond with a MazeDto json.
 * 
 */
@Path("stir")
public class MazePlay {
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{key}")
	public Response playerStir(
			@PathParam("key") String key,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String accessToken,
			@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				accessToken.equals(session.getAttribute("accessToken"))) {
		
			Grid mazeGrid = (Grid) session.getAttribute("mazegrid");
			
			try {
				mazeGrid.stirPlayer(key);
				responseStatus = 200;
				var output = new MazeDto(mazeGrid, mazeGrid.getPlayerName());
				return Response.status(responseStatus).entity(output).build();
			}
			catch (Exception err) {
				responseStatus = 406;
				return Response.status(responseStatus).entity(err.getMessage()).build();
			}
		}
		return Response.status(responseStatus).build();
	}
	
	@PUT
	@Path("/tile/{position}")
	public Response tileSelect(
			@PathParam("position") String position,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String accessToken,
			@Context HttpServletRequest request) {
		
		int posX = Character.getNumericValue(position.charAt(0));
		int posY = Character.getNumericValue(position.charAt(1));
		
		HttpSession session = request.getSession(false);

		int responseStatus = 403;

		if (userName.equals(session.getAttribute("userName")) &&
				accessToken.equals(session.getAttribute("accessToken"))) {
		
			Grid mazeGrid = (Grid) session.getAttribute("mazegrid");
			
			try {
				if(mazeGrid.selectTile(posX, posY)) {
					responseStatus = 200;
				}
				else {
					responseStatus = 204;
				}
				var output = new MazeDto(mazeGrid, mazeGrid.getPlayerName());
				return Response.status(responseStatus).entity(output).build();
			}
			catch (Exception err) {
				responseStatus = 406;
				return Response.status(responseStatus).entity(err.getMessage()).build();
			}
		}
		return Response.status(responseStatus).build();
	}
}
