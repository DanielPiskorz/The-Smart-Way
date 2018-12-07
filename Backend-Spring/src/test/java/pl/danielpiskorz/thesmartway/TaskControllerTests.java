package pl.danielpiskorz.thesmartway;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

import pl.danielpiskorz.thesmartway.controller.TaskController;
import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.service.TaskService;

@RunWith(SpringRunner.class)
public class TaskControllerTests {

	MockMvc mockMvc;

	@MockBean
	TaskService taskService;
	
	@Mock
    Principal principal;
	String mockUsername;

	@InjectMocks
	TaskController taskController;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
        mockMvc =  MockMvcBuilders.standaloneSetup(taskController).build();
        mockUsername = "coffeedrinker";
        when(principal.getName()).thenReturn(mockUsername);
	}
	
	@Test
	public void updateTodos_taskToUpdate_taskWithUpdatedTodos() throws Exception{
		//given
		long taskId = 4233;
		String[] todos = {
				"boil some water",
				"take a cup",
				"enough"
			};
		List<String> todoList = new ArrayList<>();
		for(String t : todos)
			todoList.add(t);	
		Task task = new Task("Make a coffee.", todoList, new Project());
		task.setId(taskId);
		when(taskService.updateTodos(any(String.class), any(Task.class))).thenReturn(task);
		
		//when
		mockMvc.perform(MockMvcRequestBuilders
				.patch("/api/user/tasks/" + taskId)
				.accept(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(task))
				.contentType(MediaType.APPLICATION_JSON)
				.principal(principal))
		
		//then
		      .andExpect(
		    		 status().isOk())
		      .andExpect(
		    		  jsonPath("$.name", is(task.getName())))
		      .andExpect(
		    		  jsonPath("$.todos[2]", is(todoList.get(2))));
		
	}
	
	@Test
	public void incrementTodoIndex_taskToUpdate_taskWithIncrementedTodoIndex() throws Exception{
		//given
		long taskId = 4233;
		int todoIndex = 5;
		String[] todos = {
				"boil some water",
				"take a cup",
				"enough"
			};
		List<String> todoList = new ArrayList<>();
		for(String t : todos)
			todoList.add(t);	
		Task task = new Task("Make a coffee.", todoList, new Project());
		task.setId(taskId);
		task.setCurrentTodoIndex(todoIndex);
		when(taskService.incrementTodoIndex(any(String.class), any(Long.class))).thenAnswer( new Answer<Task>() {
		    @Override
		    public Task answer(InvocationOnMock invocation) throws Throwable {
		       task.setCurrentTodoIndex(task.getCurrentTodoIndex() + 1);
		       return task;
		    }
		});
		
		//when
		mockMvc.perform(MockMvcRequestBuilders
				.patch("/api/user/tasks/" + taskId + "/done")
				.accept(MediaType.APPLICATION_JSON)
				.principal(principal))
		
		//then
		      .andExpect(
		    		 status().isOk())
		      .andExpect(
		    		  jsonPath("$.name", is(task.getName())))
		      .andExpect(
		    		  jsonPath("$.currentTodoIndex", is(todoIndex + 1)));
		
	}
}
