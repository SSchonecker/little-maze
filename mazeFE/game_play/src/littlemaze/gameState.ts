
export interface GameState {
    player: Player; // a player array contains exactly two Players
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
