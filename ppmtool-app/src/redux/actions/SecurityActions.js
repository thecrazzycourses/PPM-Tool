import axios from "axios";
import { GET_ERRORS, SET_CURRENT_USER } from "./Types";
import jwt_decode from "jwt-decode";

import setJwtToken from "../../SecurityUtil/SetJwtToken";

export const createNewUser = (newUser, history) => async dispatch => {
  try {
    await axios.post("/api/users/register", newUser);
    history.push("/login");

    // Clear Errors
    dispatch({
      type: GET_ERRORS,
      payload: {}
    });
  } catch (error) {
    // If there are errors then this will dispatch those errors to Error Reducer
    dispatch({
      type: GET_ERRORS,
      payload: error.response.data
    });
  }
};

export const login = loginRequest => async dispatch => {
  try {
    // post =>  Login Request
    const response = await axios.post("/api/users/login", loginRequest);

    // extract token from response.data
    const { token } = response.data;

    // store token in local storage
    localStorage.setItem("jwtToken", token);

    // set token in header ***
    setJwtToken(token);

    // decode token
    const decoded = jwt_decode(token);

    // dispatch to security reducer
    dispatch({
      type: SET_CURRENT_USER,
      payload: decoded
    });
  } catch (error) {
    // If there are errors then this will dispatch those errors to Error Reducer
    dispatch({
      type: GET_ERRORS,
      payload: error.response.data
    });
  }
};

export const logout = () => async dispatch => {
  localStorage.removeItem("jwtToken");
  setJwtToken(false);

  dispatch({
    type: SET_CURRENT_USER,
    payload: {}
  });
};
