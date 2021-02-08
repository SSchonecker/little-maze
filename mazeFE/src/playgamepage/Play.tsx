import React, { useState, useEffect } from "react";
import { GameState } from "../typefiles/gameState";
import styled from "styled-components";

interface PlayProps { // The type of input for the Play function
	gameState: GameState;
	onMoving(key : string) : void;
	onTileSelect(tileMessage : string, tileID : string) : void;
	playMessage : string;
	error : string;
	dropdownFunction() : void;
	resetGame() : void;
	displayPlayerInfo() : void;
	displayRules() : void;
}

const ErrorMessage = styled.p`
	height: 1em;
	color: red;
`;

const Console = styled.p`
	font-size: 1em;
	height: 100px;
	color: var(--light-sogyo);
	overflow-y: scroll;
`; // The container for the messages to the player

const Tile = styled.button`
	height: 30px;
	width: 30px;
`; // Fixed sized floor tile

export function Play({ gameState, onMoving, onTileSelect, dropdownFunction, resetGame, displayPlayerInfo, displayRules, playMessage, error }: PlayProps) {
	
	/* Forming the options */
	const resetButtonMessage = "Restart game";
	const playerButtonMessage = "Player info";
	const rulesButtonMessage = "The Rules";
	
	/* Creation of the maze grid, where a tile is a grid cell */
	let columnString = "";
	for (var count = 0; count < gameState.layout.length; count ++) {
		columnString = columnString + "auto ";
	} // the number of columns is the amount of auto's, auto means automatic resizing of the width

	async function selectTile(tileMessage : string, tileID : string) {
		let result = await onTileSelect(tileMessage, tileID);
		
		consolePrint(result);
	}

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
				tileMessage = "This looks like an ordinary tile.";
			break;
			case "c": tileStyle.backgroundColor = "yellow";
				tileMessage = "There's a chest here!";
			break;
			case "s": tileStyle.backgroundColor = "grey";
				tileMessage = "Careful now! This looks like a spikey tile...";
			break;
			case "h": tileStyle.backgroundColor = "purple";
				tileMessage = "Remain careful, this is still a spikey tile.";
			break;
			case "p": tileStyle.backgroundColor = "red";
				tileMessage = "Looks like you're here!";
		}

		return <Tile id={tileID} key={tileID} style={tileStyle} className={classes.join(" ")} onClick={() => onTileSelect(tileMessage, tileID)}></Tile>
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
		<div>Welcome to the little maze, {gameState.player.name}!</div>
		<ErrorMessage>{error}</ErrorMessage>
		
		<div id="grid" className="grid-container" style={{gridTemplateColumns: columnString}}>
			{makeGrid( gameState )}
		</div>
		
		<Console>{playMessage}</Console>
		
		<div className="dropdown">
			<button onClick={() => dropdownFunction()} className="dropbtn">Options</button>
			<div id="myDropdown" className="dropdown-content">
				<button onClick={() => displayPlayerInfo()}> {playerButtonMessage} </button>
				<button onClick={() => displayRules()}> {rulesButtonMessage} </button>
				<button className="resetbutton" onClick={() => resetGame()}>{resetButtonMessage}</button>
			</div>
		</div>
		
		<div className="movebuttons">
			<button onClick={() => onMoving("q")}> q </button>
			<button onClick={() => onMoving("w")}> w </button>
			<button onClick={() => onMoving("e")}> e </button>
			<br></br>
			<button onClick={() => onMoving("a")}> a </button>
			<button onClick={() => onMoving("s")}> s </button>
			<button onClick={() => onMoving("d")}> d </button>
		</div>
	</div>
}