import React from "react";
import PlayPage from "./playgamepage/PlayPage";
import StartGamePage from "./startgamepage/StartGamePage";
import LoginPage from "./loginpage/LoginPage";
import GamePage from "./gamepage/GamePage";
import EndGame from "./gamepage/EndGame";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

export function App() {

	return <div className="App">
      <Router>
        <Switch>
          <Route path="/" exact component={() => <LoginPage />} />
		  <Route path="/game" exact component={() => <GamePage />} />
          <Route path="/end" exact component={() => <EndGame />} />
        </Switch>
      </Router>
    </div>
}