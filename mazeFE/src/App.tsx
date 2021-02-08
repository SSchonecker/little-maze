import React from "react";
import PlayPage from "./playgamepage/PlayPage";
import StartGamePage from "./startgamepage/StartGamePage";
import LoginPage from "./loginpage/LoginPage";
import GamePage from "./gamepage/GamePage";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";

export function App() {

	return <div className="App">
      <Router>
        <Switch>
          <Route path="/" exact component={() => <LoginPage />} />
		  <Route path="/game" exact component={() => <GamePage />} />
          //<Route path="/gamestart" exact component={() => <StartGamePage />} />
          //<Route path="/playpage" exact component={() => <PlayPage />} />
        </Switch>
      </Router>
    </div>
}