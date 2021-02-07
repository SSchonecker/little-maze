import React from "react";
import { PlayPage } from "./playgamepage/PlayPage";
import { StartGamePage } from "./startgamepage/StartGamePage";
import { LoginPage } from "./loginpage/LoginPage";

export function App() {
	
	/*if (localStorage.getItem("myGameState")) {
		//return PlayPage();
		return <div>game start succes</div>;
	}*/
	console.log(localStorage.getItem("myGameState"));
	if (localStorage.getItem("myUserInfo") && localStorage.getItem("myUserInfo")!.length > 10) {
		return StartGamePage();
	}
	else {
		return LoginPage();
	}

}