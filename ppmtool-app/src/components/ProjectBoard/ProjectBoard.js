import React, { Component } from "react";
import { Link } from "react-router-dom";
import PropTypes from "prop-types";
import { connect } from "react-redux";

import Backlog from "./Backlog";
import { getBacklog } from "../../redux/actions/BacklogActions";

class ProjectBoard extends Component {
  constructor(props) {
    super(props);

    this.state = {
      errors: {}
    };
  }

  // Life Cycle Hook
  componentWillReceiveProps(nextProps) {
    if (nextProps.errors) {
      this.setState({ errors: nextProps.errors });
    }
  }

  // ProjectBoard -> Backlog Action -> Backlog Reducer -> ProjectBoard
  componentDidMount() {
    const { projectIdentifier } = this.props.match.params;
    this.props.getBacklog(projectIdentifier);
  }

  render() {
    const { projectIdentifier } = this.props.match.params;
    const { projectTasks } = this.props.backlog;
    const { errors } = this.state;

    let boardContent;

    const boardStatus = (errors, projectTasks) => {
      if (projectTasks.length < 1) {
        if (errors.projectNotFound) {
          return (
            <div className="alert alert-danger text-center" role="alert">
              {errors.projectNotFound}
            </div>
          );
        } else if (errors.projectIdentifier) {
          return (
            <div className="alert alert-danger text-center" role="alert">
              {errors.projectIdentifier}
            </div>
          );
        } else {
          return (
            <div className="alert alert-info text-center" role="alert">
              No Project Task found on this Baord!
            </div>
          );
        }
      } else {
        return <Backlog projectTasks={projectTasks} />;
      }
    };

    boardContent = boardStatus(errors, projectTasks);

    return (
      <div className="container">
        <Link
          to={`/addProjectTask/${projectIdentifier}`}
          className="btn btn-primary mb-3"
        >
          <i className="fas fa-plus-circle"> Create Project Task</i>
        </Link>
        <br />
        <hr />

        {
          // Show Board Content
        }
        {boardContent}
      </div>
    );
  }
}

ProjectBoard.propTypes = {
  backlog: PropTypes.object.isRequired,
  errors: PropTypes.object.isRequired,
  getBacklog: PropTypes.func.isRequired
};

// This state.backlog is from Project Reducer
const mapStateToProps = state => ({
  backlog: state.backlog,
  errors: state.errors
});

export default connect(mapStateToProps, { getBacklog })(ProjectBoard);
