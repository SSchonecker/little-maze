package nl.sogyo.littlemaze.mazeapi;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sogyo.littlemaze.Grid;
import nl.sogyo.littlemaze.mazeapi.dbconnect.SqlConnect;
import nl.sogyo.littlemaze.mazeapi.dtostructures.LoadMazeDto;
import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;
import nl.sogyo.littlemaze.mazeapi.dtostructures.PlayerInput;

/**
 * Class handling login requests.
 * 
 * It listens to POST requests to /start
 * or PUT requests to /start/load/{slot}, where {slot} is the slot to load from.
 * The POST listener expects a json containing playerName and gridSize fields.
 * 
 * Both only work if the correct userName and accessToken are passed in the header.
 * Both produce a grid object, that is saved in the session
 * and respond with a MazeDto json.
 * 
 */
@Path("start")
public class MazeInitiate {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String accessToken,
			PlayerInput player) {
		
		HttpSession session = request.getSession(false);

		int responseStatus = 403; // Forbidden access
		
		if (userName.equals(session.getAttribute("userName")) &&
				accessToken.equals(session.getAttribute("accessToken"))) {
			Grid mazeGrid = new Grid(Integer.parseInt(player.getGridSize()));
			String namePlayer = player.getPlayerName();
			mazeGrid.putPlayer(namePlayer);
			session.setAttribute("mazegrid", mazeGrid);
	
			var output = new MazeDto(mazeGrid, namePlayer);
			return Response.status(200).entity(output).build();
		}
		return Response.status(responseStatus).build();
	}
	
	@Path("load/{slot}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response initializeFromLoad(
			@PathParam("slot") String saveSlot,
			@Context HttpServletRequest request,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String accessToken
			) {
		
		HttpSession session = request.getSession(false);

		int responseStatus = 403; //Forbidden access
		
		if (userName.equals(session.getAttribute("userName")) &&
				accessToken.equals(session.getAttribute("accessToken"))) {
			
			SqlConnect dbConnect = new SqlConnect("jdbc:mysql://localhost:4444/maze_safe");
			
			try {
				String gameStateJSON = dbConnect.loadGame(userName, saveSlot);
				
				ObjectMapper mapper = new ObjectMapper();
				LoadMazeDto gameState = mapper.readValue(gameStateJSON, LoadMazeDto.class);
				
				Grid mazeGrid = new Grid(gameState.getPlayer().getName(),
						gameState.getPlayer().getHealth(), 
						gameState.getPlayer().getSteps(),
						gameState.getLayout());
				
				session.setAttribute("mazegrid", mazeGrid);
		
				var output = new MazeDto(mazeGrid, mazeGrid.getPlayerName());
				return Response.status(200).entity(output).build();
			}
			catch (Exception err) {
				responseStatus = 406;
				return Response.status(responseStatus).entity(err.getMessage()).build();
			}
		}
		return Response.status(responseStatus).build();
	}
}
