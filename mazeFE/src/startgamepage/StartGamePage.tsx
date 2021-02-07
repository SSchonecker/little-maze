import React, { useState, useEffect } from "react";
import { InitGame } from "./InitGame";
import { GameState } from "../typefiles/gameState";
import { LoginState } from "../typefiles/loginState";

export function StartGamePage() {
	
	/*
	 * React hooks for the over-all state of the game.
	 * If the gamestate is updated in the useState, it is also automatically added to localStorage
	 */
	const [ gameState, setGameState ] = useState<GameState | undefined>(undefined);
	const [ errorMessage, setErrorMessage ] = useState("");
	useEffect(() => {
		const json = JSON.stringify(gameState);
		localStorage.setItem("myGameState", json);
		console.log(localStorage.getItem("myGameState"));
		window.location.assign("/playgame");
	}, [gameState]);
	
	const infoState = JSON.parse(localStorage.getItem("myUserInfo")!);
	const userName = JSON.parse(localStorage.getItem("myUserInfo")!).userName;
	const token = JSON.parse(localStorage.getItem("myUserInfo")!).token;

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
		localStorage.removeItem("myGameState");

		try {
			const response = await fetch('littlemaze/api/start', {
				method: 'POST',
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json',
					'User-Name': userName,
					'Access-token': token
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
			console.log(error.toString());
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
		}
	}

	return <InitGame onPlayerConfirmed={tryStartGame}
				userName={infoState.userName}
				message={errorMessage}
	/>
}