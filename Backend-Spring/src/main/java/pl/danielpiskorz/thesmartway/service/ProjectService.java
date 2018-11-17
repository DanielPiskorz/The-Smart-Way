package pl.danielpiskorz.thesmartway.service;

import java.util.List;

import pl.danielpiskorz.thesmartway.model.Project;

public interface ProjectService {
	
	Project createProject(String username, String name);
	Project updateProject(String username, Project project);
	List<Project> getAllProjects(String username);
	void deleteProject(String username, Project project);
	
}
