import React, { useState } from "react";
import styled from "styled-components";

interface StartGameProps {
    message: string;
    onPlayerConfirmed(playerName: string, gridSize: number): void;
}

// a button element with the specified css style applied to it
const StartButton = styled.button`
    font-size: 2em;
    background-color: white;
    border: 2px solid rgb(77, 38, 0);
`

// a p element with the specified css style applied to it
const ErrorMessage = styled.p`
    height: 1em;
    color: red;
`;

/**
 * Allows the players to enter their name.
 */
export function StartGame({ message, onPlayerConfirmed }: StartGameProps) {

    const [ playerName, setPlayerName ] = useState("");
	const [ gridSize, setGridSize ] = useState(10);
    const handleKeypress = (e: React.KeyboardEvent) => {
        if (e.key === "Enter") {onPlayerConfirmed(playerName, gridSize);}
    };

    return <div className="centered">
		<input value={playerName}
               placeholder="Player name"
               onChange={(e) => setPlayerName(e.target.value)}
               onKeyPress={handleKeypress}
        />
		
		<br></br>
		<div>
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

        <StartButton onClick={() => onPlayerConfirmed(playerName, gridSize)}>
            Enter the maze!
        </StartButton>
    </div>
}