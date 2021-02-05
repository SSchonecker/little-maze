package nl.sogyo.littlemaze.mazeapi.dtostructures;

public class JsonError {
	
	private String error;
	
	public JsonError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}

}
