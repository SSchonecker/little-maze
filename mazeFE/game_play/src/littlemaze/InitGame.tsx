import React, { useState } from "react";
import styled from "styled-components";

interface StartGameProps {
    message: string;
    onPlayerConfirmed(playerName: string): void;
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
    const handleKeypress = (e: React.KeyboardEvent) => {
        if (e.key === "Enter") {onPlayerConfirmed(playerName);}
    };

    return <div className="centered">
        <input value={playerName}
               placeholder="Your name"
               onChange={(e) => setPlayerName(e.target.value)}
               onKeyPress={handleKeypress}
        />

        <ErrorMessage>{message}</ErrorMessage>

        <StartButton onClick={() => onPlayerConfirmed(playerName)}>
            Enter the maze!
        </StartButton>
    </div>
}