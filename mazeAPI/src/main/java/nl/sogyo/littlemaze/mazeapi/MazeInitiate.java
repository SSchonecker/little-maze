package nl.sogyo.littlemaze.mazeapi;

import java.sql.SQLException;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.sogyo.littlemaze.Grid;
import nl.sogyo.littlemaze.mazeapi.dbconnect.DataRow;
import nl.sogyo.littlemaze.mazeapi.dbconnect.SqlConnect;
import nl.sogyo.littlemaze.mazeapi.dtostructures.MazeDto;
import nl.sogyo.littlemaze.mazeapi.dtostructures.PlayerInput;

@Path("start")
public class MazeInitiate {
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(
			@Context HttpServletRequest request,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String token,
			PlayerInput player) {
		
		HttpSession session = request.getSession(false);

		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				token.equals(session.getAttribute("token"))) {
			Grid mazeGrid = new Grid(Integer.parseInt(player.getGridSize()));
			
			String namePlayer = player.getPlayerName();
			
			mazeGrid.putPlayer(namePlayer);
			
			session.setAttribute("mazegrid", mazeGrid);		
	
			var output = new MazeDto(mazeGrid, namePlayer);
			return Response.status(200).entity(output).build();
		}
		return Response.status(responseStatus).build();
	}
	
	/*@Path("load/{slot}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response initializeFromLoad(
			@PathParam("slot") String saveSlot,
			@Context HttpServletRequest request,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String token
			) {
		
		HttpSession session = request.getSession(false);

		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				token.equals(session.getAttribute("token"))) {
			
			SqlConnect dbConnect = new SqlConnect("jdbc:mysql://localhost:2220/maze_safe");
			
			try {
				DataRow data = dbConnect.getUserInfo(userName);
				String gameStateJSON = data.getGameStateJSON();
				ObjectMapper mapper = new ObjectMapper();
				MazeDto gameState = null;
				
				try {
					gameState = mapper.readValue(gameStateJSON, MazeDto.class);
				} catch (JsonProcessingException e) {
					throw new Exception(e.getMessage());
				}
				
				Grid mazeGrid = gameState.getGrid();
				
				mazeGrid.putPlayer(gameState.getPlayer().getName(), gameState.getPlayer().getPosition());
				
				session.setAttribute("mazegrid", mazeGrid);		
		
				var output = gameState;
				return Response.status(200).entity(output).build();
				
				responseStatus = 200;
				return Response.status(responseStatus).build();
			}
			catch (Exception err) {
				responseStatus = 406;
				return Response.status(responseStatus).entity(err.getMessage()).build();
			}
		}
		return Response.status(responseStatus).build();
	}*/
}
