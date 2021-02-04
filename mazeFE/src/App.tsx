import React, { useState, useEffect } from "react";
import { InitGame } from "./littlemaze/InitGame";
import { Play } from "./littlemaze/Play";
import { GameState } from "./littlemaze/gameState";
import { Login, InfoState } from "./littlemaze/Login";

export function App() {
	
/* Part for the login */
	const [ loginMessage, setLoginMessage ] = useState("");
	const [ infoState, setInfoState ] = useState<InfoState | undefined>(undefined);
	useEffect(() => {
		const json = localStorage.getItem("myUserInfo");
		if (json && json.length > 10) { // The length check ensures that the savedInfo from localStorage is not empty
			const savedInfo = JSON.parse(json);
			setInfoState(savedInfo);
		}
	}, []);
	useEffect(() => {
		const json = JSON.stringify(infoState);
		localStorage.setItem("myUserInfo", json);
	}, [infoState]);
	
	async function tryLoginSend(userName : string, userPassword : string, createNew : boolean) {

        if (!userName) {
            setLoginMessage("User name is required!");
            return;
        }
		
		if (!userPassword) {
			setLoginMessage("Password is required!");
			return;
		}
		
		else if (userPassword.length < 6) {
			setLoginMessage("Make your password at least 6 characters long.");
			return;
		}
		
		if (userPassword.toLowerCase().includes(userName.toLowerCase())) {
			setLoginMessage("Don't put your name in your password...");
			return;
		}

        setLoginMessage("");

        try {
            const response = await fetch('littlemaze/api/login', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ "userName": userName , "password": userPassword, "createAccount": createNew })
            });

            if (response.ok) {
                const userInfo = await response.json();
                setInfoState(userInfo);
            }
			localStorage.removeItem("myUserInfo");
            setLoginMessage("Failed to login. Try again.");
        } catch (error) {
			localStorage.removeItem("myUserInfo");
            setLoginMessage(error.toString());
        }
    }
	
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
		let userName = "";
		let token = "";
		
		if (localStorage.getItem("myUserInfo")) {
			userName = JSON.parse(localStorage.getItem("myUserInfo")!).userName;
			token = JSON.parse(localStorage.getItem("myUserInfo")!).token;
		}

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
			localStorage.removeItem("myGameState");
            setErrorMessage("Failed to start the game. Try again.");
        } catch (error) {
			localStorage.removeItem("myGameState");
			localStorage.removeItem("myUserInfo");
            setErrorMessage(error.toString());
        }
    }

	if (!infoState) {
		return <Login loginSend={tryLoginSend} message={loginMessage} />
	}
    else if (!gameState) {
        return <InitGame onPlayerConfirmed={tryStartGame}
					  userName={infoState!.userName}
                      message={errorMessage}
		/>
    }

/* The part for the play game page */
	async function MakeMove(key: string) {
		
		setErrorMessage("");
		let userName = "";
		let token = "";
		
		if (localStorage.getItem("myUserInfo")) {
			userName = JSON.parse(localStorage.getItem("myUserInfo")!).userName;
			token = JSON.parse(localStorage.getItem("myUserInfo")!).token;
		}

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
            setPlayMessage(error.toString());
		}
    }
	
	return <Play gameState={gameState} 
				 message={playMessage}
				 consolePrint={consoleTextMaker}
				 onButtonClick={MakeMove}
	/>
}