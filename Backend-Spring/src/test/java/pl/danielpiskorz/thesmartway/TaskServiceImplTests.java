package pl.danielpiskorz.thesmartway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit4.SpringRunner;

import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.model.User;
import pl.danielpiskorz.thesmartway.repository.TaskRepository;
import pl.danielpiskorz.thesmartway.service.TaskServiceImpl;

@RunWith(SpringRunner.class)
public class TaskServiceImplTests {

	@Mock
	TaskRepository taskRepository;
	
	@InjectMocks
	TaskServiceImpl taskService;
	
	Project parentProject;
	Task savedTask;

	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		
		parentProject = new Project("A project", new ArrayList<Task>());
		parentProject.setOwner(new User("bobsdad", "4rwefwwf", "fdsfdss@ffdsf.lp", "43sfdf"));
		
		when(taskRepository.save(any(Task.class))).thenAnswer(new Answer<Task>() {
		    @Override
		    public Task answer(InvocationOnMock invocation) throws Throwable {
		        Task argTask = invocation.getArgument(0);
		        
		       while( !(argTask.getId() > 0) ){
		    	   argTask.setId((long)Math.abs(Math.random()*4234));
		       }
		       
		       savedTask = argTask;
		    	return argTask;
		    }
		});
		
		when(taskRepository.findById(anyLong())).thenAnswer(new Answer<Task>() {
			@Override
			public Task answer(InvocationOnMock invocation) throws Throwable {
				long givenId = invocation.getArgument(0);
				if(givenId == savedTask.getId()){
					return savedTask;
				}else{
					return null;
				}
					
			}
		});
	}
	
	@Test
	public void incrementTodoIndex_savedTaskId_taskWithIncrementedTodoIndex(){
		//given 
		List<String> todos = new ArrayList<>();
		todos.add("win the world");
		todos.add("read 2 books");
		Task task = taskRepository.save(new Task("Yup", todos, parentProject));
		int givenIndex = task.getCurrentTodoIndex();

		//when
		Task incrementedTask = 
				taskService.incrementTodoIndex(parentProject.getOwner().getUsername(), task.getId());
		
		//then
		Assertions.assertThat(incrementedTask).isNotNull();
		Assertions.assertThat(incrementedTask.getId()).isEqualTo(task.getId());
		Assertions.assertThat(incrementedTask.getCurrentTodoIndex())
			.isEqualTo(givenIndex + 1);
	}
	
	
	@Test
	public void updateTodos_taskWithTodosToUpdate_taskWithUpdatedTodos(){
		//given
		List<String> todos = new ArrayList<>();
		todos.add("dfdsf");
		todos.add("fdsaf");
		Task task = taskRepository.save(new Task("Ordinary task.", todos, parentProject));
		
		List<String> newTodos = new ArrayList<>();
		newTodos.add("todo1");
		newTodos.add("todo2");
		newTodos.add("xdd");
		task.setTodos(newTodos);
		
		//when
		Task taskWithUpdatedTodos = 
				taskService.updateTodos(parentProject.getOwner().getUsername(), task);
		
		//then
		Assertions.assertThat(taskWithUpdatedTodos).isNotNull();
		Assertions.assertThat(taskWithUpdatedTodos.getId()).isEqualTo(task.getId());
		Assertions.assertThat(taskWithUpdatedTodos.getTodos()).isEqualTo(newTodos);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void incrementTodoIndex_wrongId_IllegalArgumentException(){
		taskService.incrementTodoIndex(parentProject.getOwner().getName(), 0);
		taskService.incrementTodoIndex(parentProject.getOwner().getName(), -43);
	}
	
	@Test(expected = NullPointerException.class)
	public void updateTodos_nullTask_NullPointerException(){
		taskService.updateTodos(parentProject.getOwner().getUsername(), null);
	}
	
	
	
}
