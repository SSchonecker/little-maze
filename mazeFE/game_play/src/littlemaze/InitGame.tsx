import React, { useState } from "react";
import styled from "styled-components";

interface StartGameProps {
    message: string;
    onPlayerConfirmed(playerName: string, gridSize: number): void;
}

// a button element with the specified css style applied to it
const StartButton = styled.button`
	background-color: black;
    font-size: 1em;
	color: white;
	border-color: white;
	position: relative;
	margin-top: 52px;
	margin-left: 87px;
`

// a p element with the specified css style applied to it
const ErrorMessage = styled.p`
    height: 1em;
    color: red;
`;

/**
 * Allows the players to enter their name.
 */
export function InitGame({ message, onPlayerConfirmed }: StartGameProps) {

    const [ playerName, setPlayerName ] = useState("");
	const [ gridSize, setGridSize ] = useState(10);
    const handleKeypress = (e: React.KeyboardEvent) => {
        if (e.key === "Enter") {onPlayerConfirmed(playerName, gridSize);}
    };

    return <div className="centered">
		<h2>Welcome to a little dungeon crawler.</h2>
		<div id="quote">
			<i>
				Find your name<br></br>
				And buried treasure<br></br>
				Face your life<br></br>
				Its pain,<br></br>
				Its pleasure<br></br>
				Leave no path untaken.<br></br>
			</i>
			<div id="author">- Neil Gaiman</div>
		</div>
		<div className="inputinfo">Enter your name:</div>
		<input value={playerName}
               onChange={(e) => setPlayerName(e.target.value)}
               onKeyPress={handleKeypress}
        />
		
		<br></br>
		<div className="inputinfo">
			Pick a maze size (between 10 and 50):
		</div>
		<input
			type="number"
			min={10} max={50}
			value={gridSize}
			onChange={(e) => setGridSize(Number(e.target.value))}
			onKeyPress={handleKeypress}
        />

        <ErrorMessage>{message}</ErrorMessage>
		
		<div id="buttonborder">
        <StartButton onClick={() => onPlayerConfirmed(playerName, gridSize)}>
            Enter the maze!
        </StartButton>
		</div>
    </div>
}