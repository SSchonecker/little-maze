import React, { useState } from "react";
import { GameState } from "./gameState";
import styled from "styled-components";

interface PlayProps { // The type of input for the Play function
    gameState: GameState;
	message: string;
	onButtonClick(key : string) : void;
}

const ErrorMessage = styled.p`
    height: 1em;
    color: red;
`;

export function Play({ gameState, message, onButtonClick }: PlayProps) {
	
	let resetButtonMessage = "Restart game";
	function resetGame() {
		localStorage.removeItem("myGameState");
		window.location.reload();
	}
	
	if (gameState.gameStatus.endgame) {
		//boardCenterMessage = "The game is over, "+gameState.gameStatus.winner+", you have won!!";
		//resetButtonMessage = "Rematch?";
		console.log(gameState.gameStatus);
    }
    
    return <div className="centered">
        <div className="centered">Welcome to the little maze, {gameState.player.name}!</div>
        <div id="grid" className="centered">
			<p>
				This is where the grid should be build. This is how it looks for now <br></br>
				{gameState.layout}
			</p>
		</div>
		<ErrorMessage>{message}</ErrorMessage>
		<button className="resetbutton" onClick={() => resetGame() }>{resetButtonMessage}</button>
    </div>
}