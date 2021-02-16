import React, { useState, useEffect, useCallback } from "react";
import { InitGame } from "./InitGame";
import { Play } from "./Play";
import { GameState } from "../typefiles/gameState";
import { LoginState } from "../typefiles/loginState";
import { withRouter , useHistory } from "react-router-dom";

/**
 * Method to handle the functionality of the /game page 
 * and get the correct components.
 *
 * It consists of two parts, one for initiating the game, one for playing it.
 */
function GamePage() {
	/*
	 * React hooks for the over-all state of the game.
	 * If the gamestate is updated in the useState, it is also automatically added to localStorage
	 */
	const [ gameState, setGameState ] = useState<GameState | undefined>(undefined);
	useEffect(() => {
		if (localStorage.getItem("myGameState") && localStorage.getItem("myGameState")!.length > 10) {
			const gameState = JSON.parse(localStorage.getItem("myGameState")!);
			setGameState(gameState);
		}
	}, []);
	useEffect(() => {
		const json = JSON.stringify(gameState);
		localStorage.setItem("myGameState", json);
	}, [gameState]);
	
	/* Creation of notifications, errorMessage is shown on the initiate game page,
	 * errorPlayMessage on the game play page.
	 * playMessage is the console text, showing updates/info on the game.
	 */
	const [ errorMessage, setErrorMessage ] = useState("");
	const [ errorPlayMessage, setErrorPlayMessage ] = useState("");
	const [ playMessage, setPlayMessage ] = useState("");
	function consolePrint( info : string ) {
		setPlayMessage(info + "\n" + playMessage);
	}
	
	/* Functions for the options menu on the game play page */
	const dropdownFunction = useCallback(() => { document.getElementById("myDropdown")!.classList.toggle("show"); }, []);
	const displayPlayerInfo = useCallback(() => {consolePrint("You have " + gameState!.player.health + " hp left, and you took " + gameState!.player.steps + " steps so far, " + gameState!.player.name);}, [gameState, consolePrint]);
	const displayRules = useCallback(() => {consolePrint("Little Maze Rules: You can move with \"w\" and \"s\" up and down, with \"a\" and \"d\" left and right. You can turn with \"q\" and \"e\". Tiles in your immediate vicinity can be checked by clicking on them. Try to find the chest without losing all your health!\n");}, [consolePrint]);
	const resetGame = () => {
		localStorage.removeItem("myGameState");
		setErrorMessage("");
		setGameState(undefined);
		setPlayMessage("");
	};
	
	useEffect(() => {
		const handleKeyDown = (e : KeyboardEvent) => {
			switch (e.keyCode) {
				case 81: MakeMove("q");
				break;
				case 87: MakeMove("w");
				break;
				case 69: MakeMove("e");
				break;
				case 65: MakeMove("a");
				break;
				case 83: MakeMove("s");
				break;
				case 68: MakeMove("d");
				break;
				case 82: displayRules(); // The r key
				break;
				case 70: displayPlayerInfo(); // The f key
				break;
				case 27: dropdownFunction(); // The esc key
				break;
			}
		};
		window.addEventListener('keydown', handleKeyDown);
		// cleanup this component
		return () => {
			window.removeEventListener('keydown', handleKeyDown);
		};
	}, [MakeMove, displayRules, displayPlayerInfo, consolePrint, dropdownFunction]);
	
	/* Info from and to the login page */
	const infoState = JSON.parse(localStorage.getItem("myUserInfo")!);
	const userName = infoState.userName;
	const accessToken = infoState.accessToken;
	const savedSlots = infoState.saveSlotUsed;
	
	const history = useHistory();
	function logout() {
		localStorage.removeItem("myGameState");
		localStorage.removeItem("myUserInfo");
		history.push('/');
	}
	
	/**
	 * Method to check the user input and get a new game from the API
	 */
	async function tryStartGame(playerName: string, gridSize: number) {

		if (!playerName) {
			setErrorMessage("Player name is required!");
			return;
		}
		
		if (gridSize > 25 || gridSize < 2) {
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
					'Access-token': accessToken
				},
				body: JSON.stringify({ nameplayer: playerName , gridSize: gridSize.toString() })
			});

			if (response.ok) {
				const newGameState = await response.json();
				consolePrint("Welcome to the little maze, " + newGameState.player.name + "!");
				setGameState(newGameState);
			}
			setErrorMessage("Failed to start the game. Try again.");
			localStorage.removeItem("myGameState");
		} catch (error) {
			console.log(error.toString());
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
		}
	}
	
	/**
	 * Method to load a game from the DB.
	 */
	async function loadGame(slot : string) {
		setErrorMessage("");
		localStorage.removeItem("myGameState");
		
		const urlPath = "littlemaze/api/start/load/" + slot;
		try {
			const response = await fetch(urlPath, {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': accessToken
				},
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

	/* As long as there is no gameState, the game initiating page is shown */
	if (!gameState) {
		return <InitGame onPlayerConfirmed={tryStartGame}
					logout={logout}
					userName={userName}
					message={errorMessage}
					savedSlots={savedSlots}
					loadGame={loadGame}
		/>
	}
	
	/* If the game has ended, the window is redirected to the score/end page */
	if (gameState!.gameStatus.endgame) {
		history.push("/end");
	}

/* If there is a gameState and the game hasn't ended, play the game */

	/**
	 * Method to send the move of a player to the API and get an updated gameState.
	 */
	async function MakeMove(key: string) {
		
		setErrorPlayMessage("");
		
		const oldHealth = gameState!.player.health;

		try {
			const urlPath = "littlemaze/api/stir/"+key;
			const response = await fetch(urlPath, {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': accessToken
			},
			});

			if (response.ok) {
				if (response.status === 200) {
					const newState = await response.json();
					if (newState.player.health < oldHealth) {
						consolePrint("Ouch! That was a spikey tile... You lost " +
							(oldHealth - newState.player.health).toString() + "hp.");
					}
					setGameState(newState);
				}
			}
		} catch (error) {
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
			setErrorPlayMessage(error.toString());
		}
	}
	
	/**
	 * Method to send the selection of a tile to the API.
	 * The tile message is contained in the tile component creator.
	 */
	async function SelectTile(tileMessage : string, tileID : string) {
		
		setErrorPlayMessage("");
		
		try {
			const urlPath = "littlemaze/api/stir/tile/"+tileID;
			const response = await fetch(urlPath, {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': accessToken
				},
			});

			if (response.ok) {
				if (response.status === 200) {
					consolePrint(tileMessage);
					const newState = await response.json();
					setGameState(newState);
				}
				else if (response.status === 204) { // This means that the selected tile is not directly next to the player
					consolePrint("You can't see this tile from here.");
				}
			}
		} catch (error) {
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
			setErrorPlayMessage(error.toString());
		}
	}
	
	/**
	 * Method to tell the API server to store the session's gameState in a particular DB slot.
	 */
	async function SaveGameFunction(slot : string) {
		
		setErrorPlayMessage("");
		if (savedSlots == 0) { // Ensures that the first slot is always filled first
			slot = "1";
		}
		
		const urlPath = "littlemaze/api/save/"+slot;
		try {
			const response = await fetch(urlPath, {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': accessToken
				},
			});

			if (response.ok) {
				setErrorPlayMessage("Successfully saved the game. The load option may not be visible until you log in again.");
			}
			else {setErrorPlayMessage("Failed to save the game. Try again.");}
		} catch (error) {
			setErrorPlayMessage(error.toString());
		}
	}
	
	return <Play gameState={gameState}
				onTileSelect={SelectTile}
				onMoving={MakeMove}
				dropdownFunction={dropdownFunction}
				resetGame={resetGame}
				displayPlayerInfo={displayPlayerInfo}
				displayRules={displayRules}
				saveGame={SaveGameFunction}
				playMessage={playMessage}
				error={errorPlayMessage}
	/>
}

export default withRouter(GamePage);