package nl.sogyo.littlemaze.mazeapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
			@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		Grid mazeGrid = (Grid) session.getAttribute("mazegrid");
		
		int responseStatus = 500;
		try {
			mazeGrid.stirPlayer(key);
			responseStatus = 200;
			var output = new MazeDto(mazeGrid, mazeGrid.getPlayerName());
			return Response.status(responseStatus).entity(output).build();
		}
		catch (Exception err) {
			responseStatus = 406;
			return Response.status(responseStatus).build();
		}
	}
	
	@PUT
	@Path("/tile/{position}")
	public Response tileSelect(
			@PathParam("position") String position,
			@Context HttpServletRequest request) {
		
		int posX = Character.getNumericValue(position.charAt(0));
		int posY = Character.getNumericValue(position.charAt(1));
		
		HttpSession session = request.getSession(false);
		Grid mazeGrid = (Grid) session.getAttribute("mazegrid");
		
		int responseStatus;
		if (mazeGrid.selectTile(posX, posY)) {
			responseStatus = 200;
			return Response.status(responseStatus).build();
		}
		else {
			responseStatus = 204;
			return Response.status(responseStatus).build();
		}
	}
}
