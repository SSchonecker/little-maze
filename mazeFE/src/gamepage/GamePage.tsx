import React, { useState, useEffect, useCallback } from "react";
import { InitGame } from "./InitGame";
import { Play } from "./Play";
import { GameState } from "../typefiles/gameState";
import { LoginState } from "../typefiles/loginState";
import { withRouter , useHistory } from "react-router-dom";

function GamePage() {
	/*
	 * React hooks for the over-all state of the game.
	 * If the gamestate is updated in the useState, it is also automatically added to localStorage
	 */
	const history = useHistory();
	
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
	
	const [ errorMessage, setErrorMessage ] = useState("");
	const [ errorPlayMessage, setErrorPlayMessage ] = useState("");
	const [ playMessage, setPlayMessage ] = useState("");
	function consolePrint( info : string ) {
		setPlayMessage(info + "\n" + playMessage);
	}
	
	const dropdownFunction = useCallback(() => { document.getElementById("myDropdown")!.classList.toggle("show"); }, []);
	const displayPlayerInfo = useCallback(() => {consolePrint("You have " + gameState!.player.health + " hp left, and you took " + gameState!.player.steps + " steps so far, " + gameState!.player.name);}, [gameState, consolePrint]);
	const displayRules = useCallback(() => {consolePrint("Little Maze Rules: You can move with \"w\" and \"s\" up and down, with \"a\" and \"d\" left and right. You can turn with \"q\" and \"e\". Tiles in your immediate vicinity can be checked by clicking on them. Try to find the chest without losing all your health!\n");}, [consolePrint]);
	const resetGame = () => {
		localStorage.removeItem("myGameState");
		setErrorMessage("");
		setGameState(undefined);
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
				case 82: displayRules();
				break;
				case 70: displayPlayerInfo();
				break;
				case 27: dropdownFunction();
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
	const token = infoState.token;
	const savedSlots = infoState.saveSlotUsed;
	function logout() {
		localStorage.removeItem("myGameState");
		localStorage.removeItem("myUserInfo");
		history.push('/');
	}
	
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
	
	async function loadGame() {
		setErrorMessage("");
		localStorage.removeItem("myGameState");

		try {
			const response = await fetch('littlemaze/api/start/load', {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': token
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

	if (!gameState) {
		return <InitGame onPlayerConfirmed={tryStartGame}
					logout={logout}
					userName={infoState.userName}
					message={errorMessage}
					savedSlots={savedSlots}
					loadGame={loadGame}
		/>
	}

/* The part for the play game page */
	async function MakeMove(key: string) {
		
		setErrorPlayMessage("");

		try {
			const urlPath = "littlemaze/api/stir/"+key;
			const response = await fetch(urlPath, {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': token
			},
			});

			if (response.ok) {
				if (response.status === 200) {
					const newState = await response.json();
					setGameState(newState);
				}
			}
		} catch (error) {
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
			setErrorPlayMessage(error.toString());
		}
	}
	
	async function SelectTile(tileMessage : string, tileID : string) {
		
		setErrorPlayMessage("");
		
		try {
			const urlPath = "littlemaze/api/stir/tile/"+tileID;
			const response = await fetch(urlPath, {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': token
				},
			});

			if (response.ok) {
				if (response.status === 200) {
					consolePrint(tileMessage);
				}
				else if (response.status === 204) {
					consolePrint("You can't see this tile from here.");
				}
			}
		} catch (error) {
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
			setErrorPlayMessage(error.toString());
		}
	}
	
	async function SaveGameFunction() {
		
		setErrorPlayMessage("");

		try {
			const response = await fetch("littlemaze/api/save/", {
				method: 'PUT',
				headers: {
					'Accept': 'application/json',
					'User-Name': userName,
					'Access-token': token
				},
			});

			if (response.ok) {
				setErrorPlayMessage("Successfully saved the game.");
			}
			else {setErrorPlayMessage("Failed to save the game. Try again.");}
		} catch (error) {
			setErrorPlayMessage(error.toString());
		}
	}
	
	/* End of game state setter */
	if (gameState!.gameStatus.endgame) {
		history.push("/end");
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