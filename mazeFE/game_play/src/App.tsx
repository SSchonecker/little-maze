import React, { useState, useEffect } from "react";
import { StartGame } from "./littlemaze/InitGame";
import { Play } from "./littlemaze/Play";
import { GameState } from "./littlemaze/gameState";

export function App() {

	/*
	 * React hooks for the over-all state of the game.
	 * At refreshing of the page, the local storage is checked for a gamestate.
	 * If the gamestate is updated in the useState, it is also automatically added to localStorage
	 */
    const [ gameState, setGameState ] = useState<GameState | undefined>(undefined);
		
	useEffect(() => {
		const json = localStorage.getItem("myGameState");
		if (json && json.length > 10) { // The length check ensures that the gamestate from localStorage is not empty
			const savedState = JSON.parse(json);
			setGameState(savedState);
		}
	}, []);
 
	useEffect(() => {
		const json = JSON.stringify(gameState);
		localStorage.setItem("myGameState", json);
	}, [gameState]);
	
	/* 
	 * Error messages to be shown on the page, one for StartGame, one for Play
	 */
    const [ errorMessage, setErrorMessage ] = useState("");
	const [ playError, setPlayError ] = useState("");

    async function tryStartGame(playerName: string) {

        if (!playerName) {
            setErrorMessage("Player name is required!");
            return;
        }

        setErrorMessage("");

        try {
            const response = await fetch('littlemaze/api/player', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nameplayer: playerName })
            });
    
            if (response.ok) {
                const gameState = await response.json();
                setGameState(gameState);
            }
            setErrorMessage("Failed to start the game. Try again.");
			localStorage.removeItem("myGameState");
        } catch (error) {
            setErrorMessage(error.toString());
			localStorage.removeItem("myGameState");
        }
    }

    if (!gameState) {
        return <StartGame onPlayerConfirmed={tryStartGame}
                          message={errorMessage}
        />
    }
	
	/*async function MakeTurn(index : number) {
		
		setPlayError("");
		
		try {
            const urlPath = "mancala/api/play/"+ index;
            const response = await fetch(urlPath, {
                method: 'PUT',
                headers: {
                    'Accept': 'application/json'
                },
            });
    
            if (response.ok) {
				if (response.status === 200) {
					const newState = await response.json();
					setGameState(newState);
				}
				else { //The API-server sends a 204 status on selecting empty pits or the wrong side
					setPlayError("Invalid move! Try again");
				}
			}
        } catch (error) {
            setPlayError(error.toString());
			localStorage.removeItem("myGameState");
		}
    }
	
	return <Play gameState={gameState} 
				 message={playError}
				 onButtonClick={MakeTurn}
	/>*/
	return <div> Todo... </div>;
}