package nl.sogyo.littlemaze.mazeapi.dbconnect;

import java.sql.Timestamp;

public class ScoreRow {
	
	private int scoreID;
	private int scorevalue; 
	private int gridSize;
	private String datetime;
	private int userID;
	
	public int getScoreID() {
		return scoreID;
	}
	public void setScoreID(int scoreID) {
		this.scoreID = scoreID;
	}
	public int getScorevalue() {
		return scorevalue;
	}
	public void setScorevalue(int scorevalue) {
		this.scorevalue = scorevalue;
	}
	public int getGridSize() {
		return gridSize;
	}
	public void setGridSize(int gridSize) {
		this.gridSize = gridSize;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(Timestamp timestamp) {
		this.datetime = timestamp.toString();
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}

}
