import React, { useState } from "react";
import styled from "styled-components";

// a p element with the specified css style applied to it
const ErrorMessage = styled.p`
	font-size: 1em;
	color: red;
`;

const PasswordNote = styled.p`
	font-size: 1em;
	background-color: white;
	color: var(--sogyo-color);
	position: fixed;
	bottom: 2%;
	width: 90%;
`;

const LoginButton = styled.button`
	background-color: black;
    font-size: 1em;
	color: white;
	border-color: white;
	margin: 2%;
`;

interface LoginProps {
	loginSend(name : string, passw : string, createNew : boolean) : void;
	message : string;
}

export function Login ( { message, loginSend } : LoginProps) {
	
	const [ userName, setUserName ] = useState("");
	const [ userPassword, setUserPassword ] = useState("");
	const handleKeypress = (e: React.KeyboardEvent) => {
		if (e.key === "Enter") {loginSend(userName, userPassword, false); setUserPassword("");}
	}; // Pressing the enter key in an input field is the same as pushing the login button
	
	function makeButtons(createNew : boolean, buttonMessage : string) {
		return <LoginButton onClick={() => {loginSend(userName, userPassword, createNew); setUserPassword("");}}>{buttonMessage}</LoginButton>
	}
	
	return <div>
		<div className="inputinfo">User name:</div>
		<input 
			value={userName}
			onChange={(e) => setUserName(e.target.value)}
			onKeyPress={handleKeypress}
		/>

		<div className="inputinfo">Password:</div>
		<input
			type="password"
			value={userPassword}
			onChange={(e) => setUserPassword(e.target.value)}
			onKeyPress={handleKeypress}
		/>
		
		<ErrorMessage>{message}</ErrorMessage>
		
		{makeButtons(false, "Login")}
		{makeButtons(true, "Create new account")}
		
		<PasswordNote>Note that the assumption is that you&apos;re smart enough to use a somewhat safe password. No one will check for entries like password123 and remind you that you shouldn&apos;t use those.</PasswordNote>
	</div>
}