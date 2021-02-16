import React from "react";
import LoginPage from "./loginpage/LoginPage";
import GamePage from "./gamepage/GamePage";
import EndGamePage from "./endgamepage/EndGamePage";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

export function App() {

	return <div className="App">
      <Router>
        <Switch>
          <Route path="/" exact component={() => <LoginPage />} />
		  <Route path="/game" exact component={() => <GamePage />} />
          <Route path="/end" exact component={() => <EndGamePage />} />
        </Switch>
      </Router>
    </div>
}