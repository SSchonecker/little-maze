import React from "react";
import { GameState } from "../typefiles/gameState";
import { EndGameBox } from "./EndGameBox";
import styled from "styled-components";
import { withRouter , useHistory } from "react-router-dom";

const Tile = styled.div`
	height: 30px;
	width: 30px;
`; // Fixed sized floor tile

function EndGame() {
	const history = useHistory();
	const gameState = JSON.parse(localStorage.getItem("myGameState")!);
	
	const newGame = () => {
		localStorage.removeItem("myGameState");
		history.push("/game");
	};
	
	/* Creation of the maze grid, where a tile is a grid cell */
	let columnString = "";
	for (var count = 0; count < gameState.layout.length; count ++) {
		columnString = columnString + "auto ";
	} // the number of columns is the amount of auto's, auto means automatic resizing of the width

	function makeGridItem(tileInfo: string, posX: number, posY: number) {
		let tileID = posX.toString() + posY.toString();
		let tileMessage = "Hm, that's a weird tile..."; // This should never turn up
		let classes = ["grid-item"];
		
		/* Setting the walls on each tile */
		if (tileInfo[1] == "_") { classes.push("topborder"); }
		if (tileInfo[2] == "_") { classes.push("rightborder"); }
		if (tileInfo[3] == "_") { classes.push("bottomborder"); }
		if (tileInfo[4] == "_") { classes.push("leftborder"); }
		
		/* Color the tiles dependent on their type */
		let tileStyle = {
			backgroundColor: "black"
		};
		switch (tileInfo.charAt(0)) {
			case "t": tileStyle.backgroundColor = "var(--light-sogyo)";
			break;
			case "c": tileStyle.backgroundColor = "yellow";
			break;
			case "s": tileStyle.backgroundColor = "var(--light-sogyo)";
			break;
			case "h": tileStyle.backgroundColor = "var(--light-sogyo)";
			break;
			case "p": tileStyle.backgroundColor = "red";
		}
		
		return <Tile id={tileID} key={tileID} style={tileStyle} className={classes.join(" ")}></Tile>
	}

	function makeGrid( gameState : GameState) {
		let tileList = [];
		for (var x = 0; x < gameState.layout.length; x++) {
			for (var y = 0; y < gameState.layout.length; y++) {
				tileList.push(makeGridItem(gameState.layout[x][y], x, y));
			}
		}
		return tileList;
	}

	return <div>
		<div id="grid" className="grid-container" style={{gridTemplateColumns: columnString}}>
			{makeGrid( gameState )}
		</div>
		
		<EndGameBox gameState = gameState />

	</div>
}

export default withRouter(EndGame);