import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import { Provider } from "react-redux";
import jwt_decode from "jwt-decode";

import "bootstrap/dist/css/bootstrap.min.css";
import "./App.css";

import Dashboard from "./components/Dashboard";
import Header from "./components/Layout/Header";
import AddProject from "./components/Project/AddProject";
import store from "./redux/store/Store";
import UpdateProject from "./components/Project/UpdateProject";
import ProjectBoard from "./components/ProjectBoard/ProjectBoard";
import AddProjectTask from "./components/ProjectBoard/ProjectTasks/AddProjectTask";
import UpdateProjectTask from "./components/ProjectBoard/ProjectTasks/UpdateProjectTask";
import LandingPage from "./components/Layout/LandingPage";
import Register from "./components/UserManagement/Register";
import Login from "./components/UserManagement/Login";
import setJwtToken from "./SecurityUtil/SetJwtToken";
import { SET_CURRENT_USER } from "./redux/actions/Types";
import { logout } from "../src/redux/actions/SecurityActions";
import SecureRoutes from "./SecurityUtil/SecureRoutes";

const jwtToken = localStorage.jwtToken;
if (jwtToken) {
  setJwtToken(jwtToken);
  const decoded = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decoded
  });

  const currentTime = Date.now() / 1000;
  if (decoded.exp < currentTime) {
    store.dispatch(logout());
    window.location.href = "/";
  }
}

function App() {
  return (
    <Provider store={store}>
      <Router>
        <div>
          <Header />

          {
            // Public Routes : LandingPage Route
          }
          <Route exact path="/" component={LandingPage} />
          <Route exact path="/register" component={Register} />
          <Route exact path="/login" component={Login} />

          {
            // Private Routes : Dashboard Route, Project Route,
            // Project Board Route, Project Task Route
          }
          <Switch>
            <SecureRoutes exact path="/dashboard" component={Dashboard} />

            {
              // Project Route
            }
            <SecureRoutes exact path="/addProject" component={AddProject} />
            <SecureRoutes
              exact
              path="/updateProject/:id"
              component={UpdateProject}
            />

            {
              // Project Board Route
            }
            <SecureRoutes
              exact
              path="/projectBoard/:projectIdentifier"
              component={ProjectBoard}
            />
            <SecureRoutes
              exact
              path="/addProjectTask/:projectIdentifier"
              component={AddProjectTask}
            />

            {
              // Project Task Route
            }
            <SecureRoutes
              exact
              path="/updateProjectTask/:projectIdentifier/:projectSequence"
              component={UpdateProjectTask}
            />
          </Switch>
        </div>
      </Router>
    </Provider>
  );
}

export default App;
