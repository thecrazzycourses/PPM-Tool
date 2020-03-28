import axios from "axios";
import {
  GET_ERRORS,
  GET_BACKLOG,
  GET_PROJECT_TASK,
  DELETE_PROJECT_TASK
} from "./Types";

// Add Project Task : This Action called from ProjectBoard
export const addProjectTask = (
  projectIdentifer,
  projectTask,
  history
) => async dispatch => {
  try {
    await axios.post(`/api/backlog/${projectIdentifer}`, projectTask);
    history.push(`/projectBoard/${projectIdentifer}`);

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

export const getBacklog = projectIdentifer => async dispatch => {
  try {
    const response = await axios.get(`/api/backlog/${projectIdentifer}`);

    dispatch({
      type: GET_BACKLOG,
      payload: response.data
    });
  } catch (error) {
    dispatch({
      type: GET_ERRORS,
      payload: error.response.data
    });
  }
};

export const getProjectTask = (
  projectIdentifer,
  projectSequence,
  history
) => async dispatch => {
  try {
    const response = await axios.get(
      `/api/backlog/${projectIdentifer}/${projectSequence}`
    );

    dispatch({
      type: GET_PROJECT_TASK,
      payload: response.data
    });
  } catch (error) {
    history.push("/dashboard");
  }
};

export const updateProjectTask = (
  projectIdentifer,
  projectSequence,
  updatedProjectTask,
  history
) => async dispatch => {
  try {
    await axios.patch(
      `/api/backlog/${projectIdentifer}/${projectSequence}`,
      updatedProjectTask
    );

    history.push(`/projectBoard/${projectIdentifer}`);

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

export const deleteProjectTask = (
  projectIdentifier,
  projectSequence
) => async dispatch => {
  if (
    window.confirm(
      `You are going to delete ${projectSequence} Project Task, Are you sure ?`
    )
  ) {
    try {
      await axios.delete(
        `/api/backlog/${projectIdentifier}/${projectSequence}`
      );

      dispatch({
        type: DELETE_PROJECT_TASK,
        payload: projectSequence
      });
    } catch (error) {
      // If there are errors then this will dispatch those errors to Error Reducer
      dispatch({
        type: GET_ERRORS,
        payload: error.response.data
      });
    }
  }
};
