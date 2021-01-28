
export interface GameState {
    players: [ Player, Player ]; // a player array contains exactly two Players
    gameStatus: GameStatus;
}

export interface Player {
    name: string;
    pits: Pit[];
    type: "player1" | "player2"; // only "player1" and "player2" are valid options for this string
    hasTurn: boolean;
}

export interface Pit {
    index: number;
    nrOfStones: number;
}

export interface GameStatus {
	winner: string;
	endOfGame: boolean;
}
