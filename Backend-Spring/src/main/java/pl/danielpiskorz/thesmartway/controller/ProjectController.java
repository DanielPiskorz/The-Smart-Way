package pl.danielpiskorz.thesmartway.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.service.ProjectService;

@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('USER')")
@RestController("/api/user/projects")
public class ProjectController {

	@Autowired
	ProjectService projectService;
	
	@GetMapping
	public ResponseEntity<List<Project>> getAllProjects(Principal principal){
		return ResponseEntity.ok(projectService.getAllProjects(principal.getName()));
	}
	
	@PostMapping
	public ResponseEntity<Project> createNewProject(Principal principal,
			@RequestBody Map<String, String> json){
		return ResponseEntity.ok(projectService.createProject(principal.getName(), json.get("name")));
	}
	
	@PutMapping
	public ResponseEntity<Project> updateProject(Principal principal,
			@RequestBody Project project){
		return ResponseEntity.ok(projectService.updateProject(principal.getName(), project));
	}
	
	@DeleteMapping
	public void deleteProject(Principal principal, @RequestBody Project project){
		projectService.deleteProject(principal.getName(), project);
	}
	
}
