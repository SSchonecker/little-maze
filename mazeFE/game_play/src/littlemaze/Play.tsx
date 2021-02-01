import React, { useState } from "react";
import { GameState } from "./gameState";
import styled from "styled-components";
import { readFileSync, readFile } from "fs";

interface PlayProps { // The type of input for the Play function
    gameState: GameState;
	message: string;
	setMessage(arg1 : string) : void;
	onButtonClick(key : string) : void;
}

const Console = styled.p`
	font-size: 1em;
	height: 100px;
	color: var(--light-sogyo);
	overflow-y: scroll;
`;

let Tile = styled.button`
		height: 30px;
		width: 30px;
	`;

export function Play({ gameState, message, setMessage, onButtonClick }: PlayProps) {
	
	function consolePrint( info : string ) {
		setMessage(info + "\n" + message);
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
	//const rulesFile = readFileSync('./gamerules.txt', 'utf-8');
	function displayRules() {
		/*readFile('./gamerules.txt', (err, rules) => {
		  if (err) throw err;
		  console.log(rules);
		});*/
		consolePrint("It would be nice to print out the rules...");
		//console.log(rulesFile);
	}
	
	function dropdownFunction() {
		document.getElementById("myDropdown")!.classList.toggle("show");
	}
	
	if (gameState.gameStatus.endgame) {
		consolePrint("The game is over!");
		consolePrint("Your score is " + gameState.gameStatus.score.toString());
    }
	
	let columnString = "";
	for (var count = 0; count < gameState.layout.length; count ++) {
		columnString = columnString + "auto ";
	}

	function makeGridItem(tileInfo: string, posX: number, posY: number) {
		let tileID = posX.toString() + posY.toString();
		let tileMessage = "Hm, that's a weird tile...";
		let classes = ["grid-item"];
		
		if (tileInfo[1] == "_") { classes.push("topborder"); }
		if (tileInfo[2] == "_") { classes.push("rightborder"); }
		if (tileInfo[3] == "_") { classes.push("bottomborder"); }
		if (tileInfo[4] == "_") { classes.push("leftborder"); }
		
		let borderStyle = {
			backgroundColor:"black"
		}
		
		switch (tileInfo.charAt(0)) {
			case "t": borderStyle.backgroundColor = "var(--light-sogyo)";
				tileMessage = "This looks like an ordinary tile.";
			break;
			case "c": borderStyle.backgroundColor = "yellow";
				tileMessage = "There's a chest here!";
			break;
			case "s": borderStyle.backgroundColor = "grey";
				tileMessage = "Careful now! This looks like a spikey tile...";
			break;
			case "h": borderStyle.backgroundColor = "grey";
				tileMessage = "Remain careful, this is still a spikey tile.";
			break;
			case "p": borderStyle.backgroundColor = "red";
				tileMessage = "Looks like you're here!";
		}
		
		return <Tile id={tileID} key={tileID} style={borderStyle} className={classes.join(" ")} onClick={() => consolePrint(tileMessage)}></Tile>
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
    
    return <div className="centered">
        <div>Welcome to the little maze, {gameState.player.name}!</div>
		<div id="grid" className="grid-container" style={{gridTemplateColumns: columnString}}>
			{makeGrid( gameState )}
		</div>
		<Console>{message}</Console>
		<div className="dropdown">
		  <button onClick={() => dropdownFunction()} className="dropbtn">Menu</button>
		  <div id="myDropdown" className="dropdown-content">
			<button onClick={() => displayPlayerInfo()}> {playerButtonMessage} </button>
			<button onClick={() => displayRules()}> {rulesButtonMessage} </button>
			<button className="resetbutton" onClick={() => resetGame()}>{resetButtonMessage}</button>
		  </div>
		</div> 
    </div>
}