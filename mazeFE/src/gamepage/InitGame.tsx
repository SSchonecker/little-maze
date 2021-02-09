import React, { useState } from "react";
import styled from "styled-components";

interface StartGameProps {
	message: string;
	userName: string;
	savedSlots: number;
	onPlayerConfirmed(playerName: string, gridSize: number): void;
	logout(): void;
	loadGame(slot : string) : void;
}

// a button element with the specified css style applied to it
const LogoutButton = styled.button`
	background-color: black;
	font-size: 1em;
	color: white;
	border-color: white;
	position: relative;
	margin-top: 52px;
	margin-left: 87px;
`;

const StartButton = styled.button`
	background-color: black;
	font-size: 1em;
	color: white;
	border-color: white;
`;

// a p element with the specified css style applied to it
const ErrorMessage = styled.p`
	height: 1em;
	color: red;
`;

const Quote = styled.div`
	width: 40%;
	position: absolute;
	bottom: 5%;
`;

/**
 * Allows the player to enter their name and set the grid size.
 */
export function InitGame({ message, userName, savedSlots, loadGame, onPlayerConfirmed, logout }: StartGameProps) {
	
	const [ playerName, setPlayerName ] = useState(userName);
	const [ gridSize, setGridSize ] = useState(10); // Default grid size of 10x10 tiles
	
	const handleKeypress = (e: React.KeyboardEvent) => {
		if (e.key === "Enter") {onPlayerConfirmed(playerName, gridSize);}
	}; // Pressing the enter key in an input field is the same as pushing the button to enter the game
	
	
	function getLoadButtons() {
		if (savedSlots == 0) {
			return <div></div>;
		}
		let buttonList = [];
		for (let i = 1; i <= savedSlots; i++) {
			buttonList.push(<StartButton key={i.toString()} onClick={() => loadGame(i.toString())}>Load game {i}</StartButton>)
		}
		return buttonList;
	}

	return <div>
		<h2>Welcome to a little dungeon crawler!</h2>
		
		<div className="inputinfo">Enter your name:</div>
		<input 
			value={playerName}
			onChange={(e) => setPlayerName(e.target.value)}
			onKeyPress={handleKeypress}
		/>

		<div className="inputinfo">
			Pick a maze size (between 2 and 25):
		</div>
		<input
			type="number"
			min={2} max={25}
			value={gridSize}
			onChange={(e) => setGridSize(Number(e.target.value))}
			onKeyPress={handleKeypress}
		/>

		<ErrorMessage>{message}</ErrorMessage>
		
		<StartButton onClick={() => onPlayerConfirmed(playerName, gridSize)}>
			Enter the maze!
		</StartButton>
		<br></br>
		{getLoadButtons()}
		
		<div id="buttonborder">
			<LogoutButton onClick={logout}>Logout
			</LogoutButton>
		</div>
		
		<Quote>
			<i>
				Find your name<br></br>
				And buried treasure<br></br>
				Face your life<br></br>
				Its pain,<br></br>
				Its pleasure<br></br>
				Leave no path untaken.<br></br>
			</i>
			<div id="author" style={{marginTop:'2%',}}>- Neil Gaiman</div>
		</Quote>
    </div>
}