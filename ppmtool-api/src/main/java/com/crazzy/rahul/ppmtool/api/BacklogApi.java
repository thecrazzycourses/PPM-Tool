package com.crazzy.rahul.ppmtool.api;

import com.crazzy.rahul.ppmtool.entity.ProjectTask;
import com.crazzy.rahul.ppmtool.services.ErrorValidationService;
import com.crazzy.rahul.ppmtool.services.ProjectTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogApi {

    private final ProjectTaskService projectTaskService;

    private final ErrorValidationService errorValidationService;

    public BacklogApi(ProjectTaskService projectTaskService, ErrorValidationService errorValidationService) {
        this.projectTaskService = projectTaskService;
        this.errorValidationService = errorValidationService;
    }

    /**
     *  {
     * 	"summary": "DSA Project Creation"
     *  }
     *
     *  {
     * 	"summary": "DSA Feature 1 Creation"
     *  }
     * @param projectTask
     * @param result
     * @param projectIdentifier
     * @return
     */
    @PostMapping("/{projectIdentifier}")
    public ResponseEntity<?> addProjectTaskToBacklog(@Valid @RequestBody ProjectTask projectTask,
                                                     BindingResult result, @PathVariable String projectIdentifier, Principal principal) {

        ResponseEntity<?> errorMap = errorValidationService.errorValidation(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask projTask = projectTaskService.addProjectTask(projectIdentifier, projectTask, principal.getName());
        return new ResponseEntity<>(projTask, HttpStatus.CREATED);
    }

    @GetMapping("/{projectIdentifier}")
    public ResponseEntity<?> getProjectBacklog(@PathVariable String projectIdentifier,Principal principal) {

        return new ResponseEntity<>(projectTaskService.findBacklogById(projectIdentifier, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/{projectIdentifier}/{projectSequence}")
    public ResponseEntity<?> getProjectTaskByProjectSequence(@PathVariable String projectIdentifier, @PathVariable String projectSequence, Principal principal) {

        ProjectTask projectTaskByProjectSequence = projectTaskService.findProjectTaskByProjectSequence(projectIdentifier, projectSequence, principal.getName());
        return new ResponseEntity<>(projectTaskByProjectSequence, HttpStatus.OK);
    }

    @PatchMapping("/{projectIdentifier}/{projectSequence}")
    public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result,
                                               @PathVariable String projectIdentifier, @PathVariable String projectSequence, Principal principal) {

        ResponseEntity<?> errorMap = errorValidationService.errorValidation(result);
        if (errorMap != null) {
            return errorMap;
        }

        ProjectTask updatedProjectTask = projectTaskService.updateProjectTaskByProjectSequence(projectTask, projectIdentifier, projectSequence, principal.getName());

        return new ResponseEntity<>(updatedProjectTask, HttpStatus.OK);
    }

    @DeleteMapping("/{projectIdentifier}/{projectSequence}")
    public ResponseEntity<?> deleteProjectTask(@PathVariable String projectIdentifier, @PathVariable String projectSequence, Principal principal) {

        projectTaskService.deleteProjectTaskByProjectSequence(projectIdentifier, projectSequence, principal.getName());
        return new ResponseEntity<>("Project Task with ID: " + projectSequence + ", deleted successfully.", HttpStatus.OK);
    }
}
