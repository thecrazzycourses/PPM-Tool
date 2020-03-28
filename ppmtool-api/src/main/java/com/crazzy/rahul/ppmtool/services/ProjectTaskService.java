package com.crazzy.rahul.ppmtool.services;

import com.crazzy.rahul.ppmtool.entity.Backlog;
import com.crazzy.rahul.ppmtool.entity.ProjectTask;
import com.crazzy.rahul.ppmtool.exception.ProjectNotFoundException;
import com.crazzy.rahul.ppmtool.repositories.BacklogRepository;
import com.crazzy.rahul.ppmtool.repositories.ProjectRepository;
import com.crazzy.rahul.ppmtool.repositories.ProjectTaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectTaskService {

    private final BacklogRepository backlogRepository;
    private final ProjectRepository projectRepository;
    private final ProjectTaskRepository projectTaskRepository;
    private final ProjectService projectService;

    public ProjectTaskService(BacklogRepository backlogRepository, ProjectRepository projectRepository, ProjectTaskRepository projectTaskRepository, ProjectService projectService) {
        this.backlogRepository = backlogRepository;
        this.projectRepository = projectRepository;
        this.projectTaskRepository = projectTaskRepository;
        this.projectService = projectService;
    }

    public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

            // project task to be added to project and project should not be null
            // Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
            Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier, username).getBacklog();

            // project task should be associated to backlog
            projectTask.setBacklog(backlog);

            // project sequence to be like IDPRJ-1, IDPRJ-2 ...100
            Integer projectTaskSequence = backlog.getProjectTaskSequence();
            projectTaskSequence++;
            backlog.setProjectTaskSequence(projectTaskSequence);

            // add sequence to project task
            projectTask.setProjectSequence(projectIdentifier + "-" + projectTaskSequence);
            projectTask.setProjectIdentifier(projectIdentifier);

            // initial priority when priority is null
            if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
                projectTask.setPriority(3);
            }

            // initial status when status is null
            if (projectTask.getStatus() == null || projectTask.getStatus() == "") {
                projectTask.setStatus("TO_DO");
            }

            return projectTaskRepository.save(projectTask);

    }

    public List<ProjectTask> findBacklogById(String projectIdentifier, String username) {

        projectService.findProjectByIdentifier(projectIdentifier, username);

        return projectTaskRepository.findByProjectIdentifierOrderByPriority(projectIdentifier);
    }

    public ProjectTask findProjectTaskByProjectSequence(String projectIdentifier, String projectSequence, String username) {

        // make sure we search on existing backlog
        projectService.findProjectByIdentifier(projectIdentifier, username);

        // make sure that our task exist
        ProjectTask projectTask = projectTaskRepository.findByProjectSequence(projectSequence);
        if (projectTask == null) {
            throw new ProjectNotFoundException("Project Task with ID : " + projectSequence + ", not found!");
        }

        // make sure backlog correspond to right project task
        if( !projectTask.getProjectIdentifier().equals(projectIdentifier)) {
            throw new ProjectNotFoundException("Project Backlog with ID : " + projectIdentifier + ", does not belong to the Project Task with ID: " + projectSequence);
        }

        return projectTask;
    }

    public ProjectTask updateProjectTaskByProjectSequence(ProjectTask updatedProjectTask, String projectIdentifier, String projectSequence,String username) {

        ProjectTask projectTask = findProjectTaskByProjectSequence(projectIdentifier, projectSequence, username);
        projectTask = updatedProjectTask;
        return projectTaskRepository.save(projectTask);
    }


    public void deleteProjectTaskByProjectSequence(String projectIdentifier, String projectSequence, String username) {

        ProjectTask projectTask = findProjectTaskByProjectSequence(projectIdentifier, projectSequence, username);
        projectTaskRepository.delete(projectTask);
    }
}
