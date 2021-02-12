package nl.sogyo.littlemaze.mazeapi;

import java.util.List;

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
import nl.sogyo.littlemaze.mazeapi.dbconnect.ScoreRow;
import nl.sogyo.littlemaze.mazeapi.dbconnect.SqlConnect;


@Path("/end")
public class MazeEnd {
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{score}")
	public Response playerStir(
			@PathParam("score") String score,
			@HeaderParam("User-Name") String userName,
			@HeaderParam("Access-Token") String token,
			@Context HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		int responseStatus = 403;
		
		if (userName.equals(session.getAttribute("userName")) &&
				token.equals(session.getAttribute("token"))) {
			
			int newScore = Integer.parseInt(score);
			
			SqlConnect dbConnect = new SqlConnect("jdbc:mysql://localhost:2220/maze_safe");
			
			try {
				List<ScoreRow> scoreRowList = dbConnect.getScores(userName, newScore, 
						((Grid) session.getAttribute("mazegrid")).getSize()); 
				responseStatus = 200;
				return Response.status(responseStatus).entity(scoreRowList).build();
			}
			catch (Exception err) {
				responseStatus = 400;
				return Response.status(responseStatus).entity(err.getMessage()).build();
			}
		}
		return Response.status(responseStatus).build();
	}
}
