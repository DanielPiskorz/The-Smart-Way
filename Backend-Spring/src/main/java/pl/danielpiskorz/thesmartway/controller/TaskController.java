package pl.danielpiskorz.thesmartway.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.service.TaskService;

@PreAuthorize("hasRole('USER')")
@RestController
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	@PatchMapping("/api/user/tasks/{taskId}")
	public Task updateTodos(Principal principal, @RequestBody Task task) {
		return taskService.updateTodos(principal.getName(), task);	
	}
	
	@PatchMapping("/api/user/tasks/{taskId}/done")
	public Task incrementTodoIndex(Principal principal, @PathVariable int taskId) {
		Map<String, Object> response = new HashMap<>();
		return taskService.incrementTodoIndex(principal.getName(), taskId);
	}
}
