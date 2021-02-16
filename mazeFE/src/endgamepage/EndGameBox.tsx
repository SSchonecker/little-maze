import React from "react";
import { GameState } from "../typefiles/gameState";
import { scoreItem } from "../typefiles/ScoreItem";
import styled from "styled-components";

const Container = styled.div`
	font-size: 1em;
	color: var(--light-sogyo);
	background-color: black;
	position: absolute;
	top: 20%;
	right: 15%;
	width: 70%;
	text-align: center;
	border-style: dotted;
	border-width: thick;
	border-color: yellow;
	border-radius: 25px;
`;

const Main = styled.p`
	font-size: 1.5em;
`;

const Info = styled.div`
	margin: 5%;
`;

const Score = styled.p`
	font-size: 1.5em;
	color: yellow;
	margin-bottom: 5%;
`;

const RestartButton = styled.button`
	background-color: black;
	font-size: 1em;
	color: white;
	border-color: white;
	position: relative;
	width: 140px;
	margin-bottom: 2%;
`;

interface BoxProps {
	gameState : GameState;
	getScoreData() : void;
	showAllScores : boolean;
	scoreList : scoreItem[];
	newGame() : void;
}

export function EndGameBox( { gameState, getScoreData, showAllScores, scoreList, newGame } : BoxProps) {
	
	// If the player lost, their negative score is shown
	if (gameState.player.health == 0) {
		return (<Container>
			<Main style={{color: "red"}}>Game over!</Main>
			<Info>Oops... you lost all your health...</Info>
			<Score>{gameState.gameStatus.score}</Score>
			<RestartButton onClick={newGame}>New grid?</RestartButton>
			</Container>);
	}
	
	/* If the player won, first their new score is shown,
	 * along with a continue option to load the scores from the DB */
	else if (!showAllScores) {
		return <Container>
			<Main style={{color: "yellow"}}>You won!</Main>
			<Info>You found the chest!<br></br>
					It took you {gameState.player.steps} steps to get here
					and you have {gameState.player.health} hp left.<br></br>
					With the chest's treasure, that leaves you with a score of:
			</Info>
			<Score>{gameState.gameStatus.score}</Score>
			<RestartButton onClick={getScoreData}>Continue</RestartButton>
			</Container>
	}

	// If continue has been pressed, show an overview of past scores from the DB
	return <Container>
			<Main style={{color: "yellow"}}>You won!</Main>
			<Score>New score: {gameState.gameStatus.score}</Score>
			<Info>
				{scoreList!.map((score, index) => (
				<div key={index}>{score.scorevalue} | Gridsize: {score.gridSize} | Date: {score.datetime} </div>
				))}
			</Info>
			<RestartButton onClick={newGame}>New grid?</RestartButton>
			</Container>
}