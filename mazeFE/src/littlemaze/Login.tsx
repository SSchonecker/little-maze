import React, { useState } from "react";
import styled from "styled-components";

export interface InfoState {
	userName : string;
}

// a p element with the specified css style applied to it
const ErrorMessage = styled.p`
    height: 1em;
    color: red;
`;

interface LoginProps {
	loginSend(name : string, passw : string) : void;
	message : string;
}

export function Login ( { message, loginSend } : LoginProps) {
	
	const [ userName, setUserName ] = useState("");
	const [ userPassword, setUserPassword ] = useState("");
	const handleKeypress = (e: React.KeyboardEvent) => {
        if (e.key === "Enter") {loginSend(userName, userPassword);}
    }; // Pressing the enter key in an input field is the same as pushing the button to enter the game
	
	
	return <div>
		<div className="inputinfo">User name:</div>
		<input 
			value={userName}
            onChange={(e) => setUserName(e.target.value)}
            onKeyPress={handleKeypress}
		/>
		<br></br>
		<div className="inputinfo">Password:</div>
		<input
			type="password"
			value={userPassword}
            onChange={(e) => setUserPassword(e.target.value)}
            onKeyPress={handleKeypress}
		/>
		<div>(Note that the assumption is that you're smart enough to use a somewhat safe password. No one will check for entries like password123 and remind you that you shouldn't use those.)</div>
		
		<ErrorMessage>{message}</ErrorMessage>
		
		<button onClick={() => loginSend(userName, userPassword)}>Login</button>
	</div>
}