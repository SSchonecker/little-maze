import React, { useState, useEffect, useCallback } from "react";
import { Play } from "./Play";
import { GameState } from "../typefiles/gameState";

export function PlayPage() {

/* The part for the play game page */
	
	const [ gamePlay, setGamePlay ] = useState<GameState>(JSON.parse(localStorage.getItem("myGameState")!));
	const [ errorPlayMessage, setErrorPlayMessage ] = useState("");
	const [ playMessage, setPlayMessage ] = useState("");
	
	const dropdownFunction = useCallback(() => { document.getElementById("myDropdown")!.classList.toggle("show"); }, []);
	const displayPlayerInfo = useCallback(() => {consolePrint("You have " + gamePlay.player.health + " hp left, and you took " + gamePlay.player.steps + " steps so far, " + gamePlay.player.name);}, [gamePlay, consolePrint]);
	const displayRules = useCallback(() => {consolePrint("Little Maze Rules: You can move with \"w\" and \"s\" up and down, with \"a\" and \"d\" left and right. You can turn with \"q\" and \"e\". Tiles in your immediate vicinity can be checked by clicking on them. Try to find the chest without losing all your health!\n");}, [consolePrint]);
	
	/*useEffect(() => {
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
				default: consolePrint("Try another key, for example \"r\" for the rules.");
			}
		};
		window.addEventListener('keydown', handleKeyDown);
		// cleanup this component
		return () => {
			window.removeEventListener('keydown', handleKeyDown);
		};
	}, [MakeMove, displayRules, displayPlayerInfo, consolePrint, dropdownFunction]);*/
	
	function consolePrint( info : string ) {
		setPlayMessage(info + "\n" + playMessage);
	}
	
	const userName = JSON.parse(localStorage.getItem("myUserInfo")!).userName;
	const token = JSON.parse(localStorage.getItem("myUserInfo")!).token;
	
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
					setGamePlay(newState);
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
	
	/* End of game state setter */
	if (gamePlay.gameStatus.endgame) {
		if (gamePlay.player.health == 0) {
			consolePrint("Oops! You lost your life...");
		}
		else {
			consolePrint("You found the chest!");
			consolePrint("The game is over!");
			consolePrint("Your score is " + gamePlay.gameStatus.score);
		}
	}
	
	return <Play gameState={gamePlay}
				onTileSelect={SelectTile}
				onMoving={MakeMove}
				dropdownFunction={dropdownFunction}
				displayPlayerInfo={displayPlayerInfo}
				displayRules={displayRules}
				playMessage={playMessage}
				error={errorPlayMessage}
	/>
	//return <div> came here </div>
}