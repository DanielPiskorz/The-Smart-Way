package pl.danielpiskorz.thesmartway;

import java.util.ArrayList;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;
import pl.danielpiskorz.thesmartway.repository.TaskRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepositoryTests {

	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ProjectRepository projectRepository;
	
	private Project parentProject;
	
	@Before
	public void setUp(){
		parentProject = projectRepository.save( new Project("Exciting Project", new ArrayList<Task>()));
	}
	
	@Test
	public void findById_presentTaskId_taskMatch(){
		//given
		Task task = new Task("Exctiting project!", new ArrayList<String>(), parentProject);
		task = taskRepository.save(task);
		
		//when
		Task foundTask = taskRepository.findById(task.getId());
		
		//then
		Assertions.assertThat(foundTask).isNotNull();
		Assertions.assertThat(foundTask.getId()).isEqualTo(foundTask.getId());
		Assertions.assertThat(foundTask.getId()).isGreaterThan(0);
		Assertions.assertThat(foundTask.getProject()).isEqualTo(parentProject);
	}
	
}
