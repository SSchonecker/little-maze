import React, { useState } from "react";
import { GameState } from "../typefiles/gameState";
import { scoreItem } from "../typefiles/ScoreItem";
import { EndGameBox } from "./EndGameBox";
import styled from "styled-components";
import { withRouter , useHistory } from "react-router-dom";

const Tile = styled.div`
	height: 30px;
	width: 30px;
`; // Fixed sized floor tile

const ErrorMessage = styled.p`
	height: 1em;
	color: red;
	position: fixed;
	bottom: 2%;
`;

function EndGame() {
	
	const gameState = JSON.parse(localStorage.getItem("myGameState")!);
	const [showScores, setShowScores] = useState(false);
	const [scoreList, setScoreList] = useState<scoreItem[]>([]);
	const [error, setError] = useState("");
	
	const infoState = JSON.parse(localStorage.getItem("myUserInfo")!);
	const userName = infoState.userName;
	const accessToken = infoState.accessToken;
	
	const history = useHistory();
	const newGame = () => {
		localStorage.removeItem("myGameState");
		history.push("/game");
	};
	
	/* Creation of the maze grid, where a tile is a grid cell */
	let columnString = "";
	for (var count = 0; count < gameState.layout.length; count ++) {
		columnString = columnString + "auto ";
	} // the number of columns is the amount of auto's, auto means automatic resizing of the width

	function makeGridItem(tileInfo: string, posX: number, posY: number) {
		let tileID = posX.toString() + posY.toString();
		let tileMessage = "Hm, that's a weird tile..."; // This should never turn up
		let classes = ["grid-item"];
		
		/* Setting the walls on each tile */
		if (tileInfo[1] == "_") { classes.push("topborder"); }
		if (tileInfo[2] == "_") { classes.push("rightborder"); }
		if (tileInfo[3] == "_") { classes.push("bottomborder"); }
		if (tileInfo[4] == "_") { classes.push("leftborder"); }
		
		/* Color the tiles dependent on their type */
		let tileStyle = {
			backgroundColor: "black"
		};
		switch (tileInfo.charAt(0)) {
			case "t": tileStyle.backgroundColor = "var(--light-sogyo)";
			break;
			case "c": tileStyle.backgroundColor = "yellow";
			break;
			case "s": tileStyle.backgroundColor = "var(--light-sogyo)";
			break;
			case "h": tileStyle.backgroundColor = "var(--light-sogyo)";
			break;
			case "p": tileStyle.backgroundColor = "red";
		}
		
		return <Tile id={tileID} key={tileID} style={tileStyle} className={classes.join(" ")}></Tile>
	}

	function makeGrid( gameState : GameState) {
		let tileList = [];
		for (var x = 0; x < gameState.layout.length; x++) {
			for (var y = 0; y < gameState.layout.length; y++) {
				tileList.push(makeGridItem(gameState.layout[x][y], x, y));
			}
		}
		return tileList;
	}
	
	async function getScoreData() {
		setError("");
		
		const urlPath = "littlemaze/api/end/" + gameState.gameStatus.score;
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
				const scores = await response.json();
				setScoreList(scores);
			}
		} catch (error) {
			console.log(error.toString());
			setError(error.toString());
		}
		
		setShowScores(true);
	}

	return <div>
		<div id="grid" className="grid-container" style={{gridTemplateColumns: columnString}}>
			{makeGrid( gameState )}
		</div>
		
		<EndGameBox gameState={gameState}
					getScoreData={getScoreData}
					showAllScores={showScores}
					scoreList={scoreList}
					newGame={newGame}/>
					
		<ErrorMessage>{error}</ErrorMessage>

	</div>
}

export default withRouter(EndGame);