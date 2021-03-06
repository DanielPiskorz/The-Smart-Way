package pl.danielpiskorz.thesmartway;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.model.User;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;
import pl.danielpiskorz.thesmartway.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProjectRepositoryTests{
	
	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	private User projectOwner;
	
	@Before
	public void setUpOwner(){
		projectOwner = userRepository.save(
				new User("George", "george123", "happyGeorge123@gmail.com", "ilikeflowers123"));
		
	}
	
	@Test
	public void findById_savedProject_matchingProject(){
		//given
		Project project = new Project("Awesome project", new ArrayList<Task>());
		project.setOwner(projectOwner);
		project = projectRepository.save(project);
		
		//when
		Project found = projectRepository.findById(project.getId());
		
		//then
		Assertions.assertThat(found).isNotNull();
		Assertions.assertThat(found.getName()).isEqualTo(project.getName());	
		Assertions.assertThat(project.getOwner()).isEqualTo(projectOwner);
		
	}
	
	public void findAllProjectsByOwnerUsername_validUsername_allProjects(){
		//given
		Project[] projects = new Project[] {
				new Project("Awesome project", new ArrayList<Task>()),
				new Project("Another..", new ArrayList<Task>()),
				new Project("Third excting project xd", new ArrayList<Task>())};
		
		for(Project project : projects){
			project.setOwner(projectOwner);
			projectOwner.getProjects().add(project);
			project = projectRepository.save(project);
		}
		
		//when
		List<Project> foundProjects = 
				projectRepository.findAllProjectsByOwnerUsername(projectOwner.getName());
		
		//then
		Assertions.assertThat(foundProjects).isNotNull();
		Assertions.assertThat(foundProjects.size()).isEqualTo(projects.length);
		Assertions.assertThat(foundProjects.get(1)).isEqualTo(projects[1]);
	}	
}
