import React, { useState, useEffect } from "react";
import { InitGame } from "./littlemaze/InitGame";
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
	 * Error messages to be shown on the InitGame page
	 * and messages for the Play page
	 */
    const [ errorMessage, setErrorMessage ] = useState("");
	const [ playMessage, setPlayMessage ] = useState("");
	function consoleTextMaker( info : string ) {
		setPlayMessage(info + "\n" + playMessage);
	}

/* The part for the start game page */
    async function tryStartGame(playerName: string, gridSize: number) {

        if (!playerName) {
            setErrorMessage("Player name is required!");
            return;
        }
		
		if (gridSize > 50 || gridSize < 2) {
			setErrorMessage("Pick a decent grid size!");
			return;
		}

        setErrorMessage("");

        try {
            const response = await fetch('littlemaze/api/start', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ nameplayer: playerName , gridSize: gridSize.toString() })
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
        return <InitGame onPlayerConfirmed={tryStartGame}
                          message={errorMessage}
        />
    }

/* The part for the play game page */
	async function MakeMove(key: string) {
		try {
            const urlPath = "littlemaze/api/stir/"+key;
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
			}
        } catch (error) {
            setPlayMessage(error.toString());
			localStorage.removeItem("myGameState");
		}
    }
	
	return <Play gameState={gameState} 
				 message={playMessage}
				 consolePrint={consoleTextMaker}
				 onButtonClick={MakeMove}
	/>
}