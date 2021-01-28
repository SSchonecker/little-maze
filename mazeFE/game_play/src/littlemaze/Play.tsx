import React, { useState } from "react";
import { GameState } from "./gameState";
import styled from "styled-components";

interface PlayProps { // The type of input for the Play function
    gameState: GameState;
	message: string;
	onButtonClick(index : number) : void;
}

const ErrorMessage = styled.p`
    height: 1em;
    color: red;
`;

export function Play({ gameState, message, onButtonClick }: PlayProps) {
    let pitsOne = gameState.players[0].pits;
    let pitsTwo = gameState.players[1].pits;
    let kalahaOne = pitsOne[pitsOne.length - 1];
    let kalahaTwo = pitsTwo[pitsTwo.length - 1];
    let pitsOnlyOne = pitsOne.slice(0,-1);
    let pitsOnlyTwo = pitsTwo.slice(0,-1);
	
	let resetButtonMessage = "Restart game";
	function resetGame() {
		localStorage.removeItem("myGameState");
		window.location.reload();
	}
	
	let boardCenterMessage = gameState.players[0].name + ", your turn!";
	if (gameState.players[1].hasTurn) {boardCenterMessage = gameState.players[1].name + ", your turn!";}
	
	if (gameState.gameStatus.endOfGame) {
		boardCenterMessage = "The game is over, "+gameState.gameStatus.winner+", you have won!!";
		resetButtonMessage = "Rematch?";
		console.log(gameState.gameStatus);
    }
    
    return <div className="centered">
        <div className="centered">{gameState.players[0].name} vs {gameState.players[1].name}</div>
        <div id="mancalaboard" className="centered">
			<p id="toprow" className="row">
				{pitsOnlyTwo.reverse().map(pit =>
					<span className="pit" key={pit.index}>
						{pit.nrOfStones}
						<br></br>
						<button className="pitbutton" onClick={() => onButtonClick(pit.index)}>{pit.index}</button>
					</span>
				)}
				<span id="sideone" className="sidetext">{gameState.players[1].name}'s side</span>
			</p>
			<p id="middlerow" className="row">
				<span id="kalahatwo" className="pit" key={kalahaTwo.index}>
					{kalahaTwo.nrOfStones}
				</span>
				<span id="boardcenter"> {boardCenterMessage} </span>
				<span id="kalahaone" className="pit" key={kalahaOne.index}>
					{kalahaOne.nrOfStones}
				</span>
			</p>
			<p id="bottomrow" className="row">
				<span className="sidetext">{gameState.players[0].name}'s side</span>
				{pitsOnlyOne.map(pit =>
					<span className="pit" key={pit.index}>
						{pit.nrOfStones}
						<br></br>
						<button className="pitbutton" onClick={() => onButtonClick(pit.index)}>{pit.index}</button>
					</span>
				)}
			</p>
		</div>
		<ErrorMessage>{message}</ErrorMessage>
		<button className="resetbutton" onClick={() => resetGame() }>{resetButtonMessage}</button>
    </div>
}