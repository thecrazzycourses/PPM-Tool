import { createStore, applyMiddleware, compose } from "redux";
import thunk from "redux-thunk";
import rootReducer from "../reducers/Reducer";

const initialState = {};

const middleware = [thunk];

let store;
const ReactReduxDevTool =
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__();

if (window.navigator.userAgent.includes("Firefox") && ReactReduxDevTool) {
  store = createStore(
    // Root Reducer as Reducer
    rootReducer,
    initialState,
    compose(applyMiddleware(...middleware), ReactReduxDevTool)
  );
} else {
  store = createStore(
    rootReducer,
    initialState,
    compose(applyMiddleware(...middleware))
  );
}

export default store;
