import React, { useState, useEffect } from "react";
import { Login } from "./Login";
import { LoginState } from "../typefiles/loginState";
import { withRouter , useHistory } from "react-router-dom";

function LoginPage() {

	const [ loginMessage, setLoginMessage ] = useState("");
	const [ infoState, setInfoState ] = useState<LoginState | undefined>(undefined);
	useEffect(() => {
		const json = JSON.stringify(infoState);
		localStorage.setItem("myUserInfo", json);
	}, [infoState]); // Add the login info to the localStorage
	
	const history = useHistory(); // Required to change the page
	
	/**
	 * Method to check the userName and the password and send a login request to the API	
	 */
	async function tryLoginSend(userName : string, userPassword : string, createNew : boolean) {

		setLoginMessage("");

		if (!userName) {
			setLoginMessage("User name is required!");
			return;
		}
		else if (!userName.match("^[A-Za-z0-9]+$")) {
			setLoginMessage("No special characters!");
			return;
		}
		
		if (!userPassword) {
			setLoginMessage("Password is required!");
			return;
		}
		else if (userPassword.length < 10) {
			setLoginMessage("Make your password at least 10 characters long.");
			return;
		}
		else if (userPassword.toLowerCase().includes(userName.toLowerCase())) {
			setLoginMessage("Don't put your name in your password...");
			return;
		}

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
				history.push('/game'); // Send the user to the game page
			}
			else if (response.status == 406) {
				const errorInfo = await response.json();
				localStorage.removeItem("myUserInfo");
				setLoginMessage("Failed to login. The error was: " + errorInfo.error);
			}
		} catch (error) {
			localStorage.removeItem("myUserInfo");
			setLoginMessage(error.toString());
		}
	}
	
	return <Login loginSend={tryLoginSend} message={loginMessage} />
}

export default withRouter(LoginPage);