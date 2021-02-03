import React, { useState, useEffect } from "react";
import { GameState } from "./gameState";
import styled from "styled-components";

interface PlayProps { // The type of input for the Play function
    gameState: GameState;
	onMoving(key : string) : void;
	onTileSelect(tileMessage : string, tileID : string) : Promise<string>;
}

const Console = styled.p`
	font-size: 1em;
	height: 100px;
	color: var(--light-sogyo);
	overflow-y: scroll;
`; // The container for the messages to the player

let Tile = styled.button`
	height: 30px;
	width: 30px;
`; // Fixed sized floor tile

export function Play({ gameState, onMoving, onTileSelect }: PlayProps) {
	
	const [ playMessage, setPlayMessage ] = useState("");
	function consolePrint( info : string ) {
		setPlayMessage(info + "\n" + playMessage);
	}
	
	/* Forming the options */
	function dropdownFunction() {
		document.getElementById("myDropdown")!.classList.toggle("show");
	}
	
	let resetButtonMessage = "Restart game";
	function resetGame() {
		localStorage.removeItem("myGameState");
		window.location.reload();
	}
	
	let playerButtonMessage = "Player info";
	function displayPlayerInfo() {
		consolePrint("You have " + gameState.player.health + " hp left, and you took " + gameState.player.steps + " steps so far, " + gameState.player.name);
	}
	
	let rulesButtonMessage = "The Rules";
	function displayRules() {
		consolePrint("Little Maze Rules: You can move with \"w\" and \"s\" up and down, with \"a\" and \"d\" left and right. You can turn with \"q\" and \"e\". Tiles in your immediate vicinity can be checked by clicking on them. Try to find the chest without losing all your health!\n");
	}
	
	/* End of game state setter */
	if (gameState.gameStatus.endgame) {
		consolePrint("The game is over!");
		consolePrint("Your score is " + gameState.gameStatus.score);
    }
	
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
		
		return <Tile id={tileID} key={tileID} style={tileStyle} className={classes.join(" ")} onClick={() => selectTile(tileMessage, tileID)}></Tile>
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
	
	const handleKeyDown = (e : KeyboardEvent) => {
		switch (e.keyCode) {
			case 81: onMoving("q");
			break;
			case 87: onMoving("w");
			break;
			case 69: onMoving("e");
			break;
			case 65: onMoving("a");
			break;
			case 83: onMoving("s");
			break;
			case 68: onMoving("d");
			break;
			case 82: displayRules();
			break;
			case 70: displayPlayerInfo();
			break;
			case 27: dropdownFunction();
			break;
			default: consolePrint("Try another key, for example \"r\" for the rules.");
		}
	};
	
	useEffect(() => {
		window.addEventListener('keydown', handleKeyDown);

		// cleanup this component
		return () => {
		  window.removeEventListener('keydown', handleKeyDown);
		};
	}, []);
    
    return <div>
        <div>Welcome to the little maze, {gameState.player.name}!</div>
		
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