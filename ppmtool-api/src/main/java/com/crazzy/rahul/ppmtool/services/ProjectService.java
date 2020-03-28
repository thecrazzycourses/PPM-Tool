package com.crazzy.rahul.ppmtool.services;

import com.crazzy.rahul.ppmtool.entity.Backlog;
import com.crazzy.rahul.ppmtool.entity.Project;
import com.crazzy.rahul.ppmtool.entity.User;
import com.crazzy.rahul.ppmtool.exception.ProjectIdException;
import com.crazzy.rahul.ppmtool.exception.ProjectNotFoundException;
import com.crazzy.rahul.ppmtool.repositories.BacklogRepository;
import com.crazzy.rahul.ppmtool.repositories.ProjectRepository;
import com.crazzy.rahul.ppmtool.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final BacklogRepository backlogRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, BacklogRepository backlogRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.backlogRepository = backlogRepository;
        this.userRepository = userRepository;
    }

    public Project saveOrUpdate(Project project, String username) {

        // if its an update
        if (project.getId() != null) {
            Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());
            if (existingProject != null && (!existingProject.getProjectLeader().equals(username))) {
                throw new ProjectNotFoundException("Project not found in your account");
            } else if ( existingProject == null) {
                throw new ProjectNotFoundException("Project with ID: " + project.getId() + ", cannot be updated because it does not exist");
            }
        }


        try {

            // Set User to Project
            User user = userRepository.findByUsername(username);
            project.setUser(user);
            project.setProjectLeader(user.getUsername());

            project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

            // If we are creating a new Project that means we dont have an ID then we need to create Backlog
            if (project.getId() == null) {
                Backlog backlog = new Backlog();

                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
            }

            if (project.getId() != null) {
                Backlog backlog = backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());
                project.setBacklog(backlog);
            }

            return projectRepository.save(project);
        } catch (Exception ex) {
            throw new ProjectIdException("Project ID " + project.getProjectIdentifier().toUpperCase() + " is already exist");
        }

    }

    public Project findProjectByIdentifier(String projectIdentifier, String username) {

        // Project byProjectIdentifier = projectRepository.findByProjectIdentifier(projectIdentifier.toUpperCase());
        Project byProjectIdentifier = projectRepository.findByProjectIdentifierAndProjectLeader(projectIdentifier.toUpperCase(), username);
        if (byProjectIdentifier == null) {
            throw new ProjectIdException("Project ID " + projectIdentifier.toUpperCase() + " dosen't exist");
        }
        return byProjectIdentifier;
    }

    public Iterable<Project> findAllProjects(String username) {
        return projectRepository.findAllByProjectLeader(username);
    }

    public void deleteProjectByIdentifier(String projectIdentifier, String username) {
        projectRepository.delete(findProjectByIdentifier(projectIdentifier, username));
    }
}
