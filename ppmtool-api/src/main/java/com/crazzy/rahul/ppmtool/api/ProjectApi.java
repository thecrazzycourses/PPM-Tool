package com.crazzy.rahul.ppmtool.api;

import com.crazzy.rahul.ppmtool.entity.Project;
import com.crazzy.rahul.ppmtool.services.ErrorValidationService;
import com.crazzy.rahul.ppmtool.services.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectApi {

    private final ProjectService projectService;

    private final ErrorValidationService errorValidationService;

    public ProjectApi(ProjectService projectService, ErrorValidationService errorValidationService) {
        this.projectService = projectService;
        this.errorValidationService = errorValidationService;
    }


    /**
     *  {
     * 	"projectName": "DSA",
     * 	"projectIdentifier": "IDDSA",
     * 	"description": "Dynamic Survey Application"
     * }
     *
     *
     * {
     * 	"projectName": "QRE",
     * 	"projectIdentifier": "IDQRE",
     * 	"description": "Quality Related Event Application"
     * }
     *
     * @param project
     * @param result
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> createProject(@Valid @RequestBody Project project, BindingResult result, Principal principal) {

        ResponseEntity<?> errorMap = errorValidationService.errorValidation(result);
        if (errorMap != null) {
            return errorMap;
        }

        Project prj = projectService.saveOrUpdate(project, principal.getName());
        return new ResponseEntity<>(prj, HttpStatus.CREATED);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> findProjectByIdentifier(@PathVariable String projectId, Principal principal) {
        Project projectByIdentifier = projectService.findProjectByIdentifier(projectId, principal.getName());
        return new ResponseEntity<>(projectByIdentifier, HttpStatus.OK);
    }

    @GetMapping("/all")
    public Iterable<Project> findAllProjects(Principal principal) {
        return projectService.findAllProjects(principal.getName());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal) {
        projectService.deleteProjectByIdentifier(projectId,principal.getName());
        return new ResponseEntity<>("Project with ID " + projectId.toUpperCase() + " deleted successfully ", HttpStatus.OK);
    }
}
