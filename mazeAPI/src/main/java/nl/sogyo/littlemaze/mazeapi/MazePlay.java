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

@Path("stir")
public class MazePlay {
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{key}")
	public Response playerStir(
			@PathParam("key") String key,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String token,
			@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				token.equals(session.getAttribute("token"))) {
		
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
			@HeaderParam("Access-Token") String token,
			@Context HttpServletRequest request) {
		
		int posX = Character.getNumericValue(position.charAt(0));
		int posY = Character.getNumericValue(position.charAt(1));
		
		HttpSession session = request.getSession(false);

		int responseStatus = 403;

		if (userName.equals(session.getAttribute("userName")) &&
				token.equals(session.getAttribute("token"))) {
		
			Grid mazeGrid = (Grid) session.getAttribute("mazegrid");
			
			try {
				mazeGrid.selectTile(posX, posY);
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
}
