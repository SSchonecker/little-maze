import React from "react";
import { GameState } from "../typefiles/gameState";
import styled from "styled-components";
import { withRouter , useHistory } from "react-router-dom";

const Container = styled.div`
	font-size: 1em;
	color: var(--light-sogyo);
	background-color: black;
	position: absolute;
	top: 40%;
	right: 20%;
	width: 60%;
	height: 30%;
	text-align: center;
	border-style: dotted;
	border-width: thick;
	border-color: yellow;
	border-radius: 25px;
`;

const Main = styled.p`
	font-size: 1.5em;
`;

const Info = styled.p`

`;

const Score = styled.p`
	font-size: 1.5em;
	color: yellow;
`;

const Tile = styled.div`
	height: 30px;
	width: 30px;
`; // Fixed sized floor tile

const RestartButton = styled.button`
	background-color: black;
	font-size: 1em;
	color: white;
	border-color: white;
	position: absolute;
	bottom: 20%;
	left: 46%;
	width: 8%;
`;

function EndGame() {
	const history = useHistory();
	const gameState = JSON.parse(localStorage.getItem("myGameState")!);
	
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
	
	function InfoBox() {
		if (gameState.player.health == 0) {
			return (<Container>
				<Main style={{color: "red"}}>Game over!</Main>
				<Info>Oops... you lost all your health...</Info>
				</Container>);
		}
		
		return <Container>
			<Main style={{color: "yellow"}}>You won!</Main>
			<Info>You found the chest!<br></br>
					It took you {gameState.player.steps} steps to get here
					and you have {gameState.player.health} hp left.<br></br>
					With the chest's treasure, that leaves you with a score of:
			</Info>
			<Score>{gameState.gameStatus.score}</Score>
			</Container>
		
	}

	return <div>
		<div id="grid" className="grid-container" style={{gridTemplateColumns: columnString}}>
			{makeGrid( gameState )}
		</div>
		
		{InfoBox()}
		
		<RestartButton onClick={newGame}>New grid?</RestartButton>

	</div>
}

export default withRouter(EndGame);