/* Interfaces for these objects for typescript */
export interface GameState {
    player: Player;
    gameStatus: GameStatus;
	layout: string[][];
}

interface Player {
    name: string;
	health: number;
	steps: number;
	position: number[];
}

interface GameStatus {
	endgame: boolean;
	score: number;
}
