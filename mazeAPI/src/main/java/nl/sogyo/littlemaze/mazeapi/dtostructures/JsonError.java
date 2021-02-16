package nl.sogyo.littlemaze.mazeapi.dtostructures;

/**
 * This class turns an error message into an object
 * to be sent as json. It is used in MazeLogin
 *
 */
public class JsonError {
	
	private String error;

	public JsonError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

}
