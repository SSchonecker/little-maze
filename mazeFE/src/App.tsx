import React from "react";
import { PlayPage } from "./playgamepage/PlayPage";
import { StartGamePage } from "./startgamepage/StartGamePage";
import { LoginPage } from "./loginpage/LoginPage";

export function App() {
	
	if (localStorage.getItem("myGameState") && localStorage.getItem("myGameState")!.length > 10) {
		return PlayPage();
	}
	else if (localStorage.getItem("myUserInfo") && localStorage.getItem("myUserInfo")!.length > 10) {
		return StartGamePage();
	}
	else {
		return LoginPage();
	}
	
}