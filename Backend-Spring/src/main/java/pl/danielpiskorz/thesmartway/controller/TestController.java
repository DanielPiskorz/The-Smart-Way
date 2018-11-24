package pl.danielpiskorz.thesmartway.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;

@RestController
public class TestController {

	@Autowired
	ProjectRepository projectRepository;
	
	@GetMapping("/api/test")
	public ResponseEntity<String> testIt(@RequestHeader("Authorization") String auth){
		System.out.println(auth);
		return ResponseEntity.ok(auth);
	}
	
	@PostMapping("api/test")
	public ResponseEntity<Project> setTasks(@RequestBody List<String> todoss){
		Project project = projectRepository.findById(1);
		project.getTasks().get(0).setTodos(todoss);
		projectRepository.save(project);
		return ResponseEntity.ok(projectRepository.findById(1));
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("api/user")
	public ResponseEntity<Principal> userDetails(Principal principal) {
		return ResponseEntity.ok(principal);
	}
	
	@PreAuthorize("hasRole('USER')")
	@GetMapping("api/user/projects")
	public ResponseEntity<List<Project>> getUserProjects(Principal principal) {
		List<Project> projects = projectRepository.findAllProjectsByOwnerUsername(principal.getName());
		return ResponseEntity.ok(projects);
	}
}
