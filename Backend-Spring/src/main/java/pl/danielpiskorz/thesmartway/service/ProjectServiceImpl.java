package pl.danielpiskorz.thesmartway.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;
import pl.danielpiskorz.thesmartway.repository.UserRepository;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	UserRepository userRepository;

	private static final Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	
	@Override
	public Project createProject(String username, String name) {
		Project project = new Project();
		project.setName(name);
		project.setOwner(userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("Username not found")));
		assignTasks(project);
		return projectRepository.save(project);
	}

	@Override
	public Project updateProject(String username, Project project) {
		assignTasks(project);
		return projectRepository.save(project);
	}

	@Override
	public List<Project> getAllProjects(String username) {
		return projectRepository.findAllProjectsByOwnerUsername(username);
	}

	@Override
	public void deleteProject(String username, long projectId) {
		if(userRepository.findByUsername(username).get().getProjects().stream()
				.anyMatch(p -> p.getId() == projectId )){

			projectRepository.deleteById(projectId);
		}
	}
	
	private void assignTasks(Project project) {
		project.getTasks().stream().forEach(p -> p.setProject(project));
	}
}
