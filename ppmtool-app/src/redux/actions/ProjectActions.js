import axios from "axios";
import { GET_ERRORS, GET_PROJECTS, GET_PROJECT, DELETE_PROJECT } from "./Types";

// Create/Update New Project : This Action called from AddProject onSubmit()
export const createProject = (project, history) => async dispatch => {
  try {
    await axios.post("/api/project", project);
    history.push("/dashboard");

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

// Get All Projects : This Action called from Dashboard componentDidMount()
export const getProjects = () => async dispatch => {
  const response = await axios.get("/api/project/all");
  dispatch({
    type: GET_PROJECTS,
    payload: response.data
  });
};

// Get Project By Identifier : This Action called from UpdateProject componentDidMount()
export const getProjectByIdentifier = (
  projectIdentifier,
  history
) => async dispatch => {
  try {
    const response = await axios.get(`/api/project/${projectIdentifier}`);
    dispatch({
      type: GET_PROJECT,
      payload: response.data
    });
  } catch (error) {
    history.push("/dashboard");
  }
};

// Delete Project By Identifier : This Action called from Dashboard delete button
export const deleteProjectByIdentifier = projectIdentifier => async dispatch => {
  if (
    window.confirm(
      `You are going to delete ${projectIdentifier} Project, Are you sure ?`
    )
  ) {
    try {
      await axios.delete(`/api/project/${projectIdentifier}`);
      dispatch({
        type: DELETE_PROJECT,
        payload: projectIdentifier
      });
    } catch (error) {}
  }
};
