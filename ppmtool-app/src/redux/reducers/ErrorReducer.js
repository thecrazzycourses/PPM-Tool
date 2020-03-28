import { GET_ERRORS } from "../actions/Types";

const initialState = {};

export default function(state = initialState, action) {
  switch (action.type) {
    // Error Reducer
    case GET_ERRORS:
      // Return Action payload to Reducer
      return action.payload;

    default:
      return state;
  }
}
