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

import nl.sogyo.littlemaze.Grid;
import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;
import nl.sogyo.littlemaze.mazeapi.dtostructures.PlayerInput;

@Path("start")
public class MazeInitiate {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request, 
			PlayerInput player) {
		
		HttpSession session = request.getSession(true);
		Grid mazeGrid = new Grid(Integer.parseInt(player.getGridSize()));
		
		String namePlayer = player.getPlayerName();
		
		mazeGrid.putPlayer(namePlayer);
		
		session.setAttribute("mazegrid", mazeGrid);		

		var output = new MazeDto(mazeGrid, namePlayer);
		return Response.status(200).entity(output).build();
	}

}
