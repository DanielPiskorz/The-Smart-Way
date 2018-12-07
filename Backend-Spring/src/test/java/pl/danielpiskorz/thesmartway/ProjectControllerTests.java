package pl.danielpiskorz.thesmartway;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

import pl.danielpiskorz.thesmartway.controller.ProjectController;
import pl.danielpiskorz.thesmartway.model.Project;
import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.service.ProjectService;

@RunWith(SpringRunner.class)
public class ProjectControllerTests {

	MockMvc mockMvc;

	@MockBean
	ProjectService projectService;
	
	@Mock
    Principal principal;
	String mockUsername;

	@InjectMocks
	ProjectController projectController;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
        mockMvc =  MockMvcBuilders.standaloneSetup(projectController).build();
        mockUsername = "sdfasfsegf4tr";
        when(principal.getName()).thenReturn(mockUsername);
	}
	
	@Test
	public void getAllProjects_validCredentials_projectsList() throws Exception {
		//given
		List<Project> projects= Arrays.asList(new Project[]{
				new Project("Fun project xd", new ArrayList<Task>()),
				new Project("another", new ArrayList<Task>())
		});;

		when(projectService.getAllProjects(mockUsername)).thenReturn(projects);
		
		//when
		mockMvc.perform(MockMvcRequestBuilders
				.get("/api/user/projects")
				.principal(principal))
		
		//then
		      .andExpect(
		    		 status().isOk())
		      .andExpect(
		    		  jsonPath("$", hasSize(projects.size())))
		      .andExpect(
		    		  jsonPath("$[1].name", is(projects.get(1).getName())));

		
	}
	
	@Test
	public void createNewProject_validName_savedProject() throws Exception {
		//given
		String projectName = "Think different.";
		when(projectService.createProject(mockUsername, projectName)).thenReturn(
				new Project(projectName, new ArrayList<Task>()));
		
		
		//when
		mockMvc.perform(MockMvcRequestBuilders
				.post("/api/user/projects")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(String.format("{\"name\": \"%s\"}", projectName))
				.principal(principal))
		
		//then
		      .andExpect(
		    		 status().isOk())
		      .andExpect(
		    		  jsonPath("$.name", is(projectName)));
		
	}
	
	@Test
	public void updateProject_savedProjectWithChanges_updatedProject() throws Exception {
		//given
		Project updatedProject = new Project("Just do it xD.", new ArrayList<Task>());
		updatedProject.setId(4324);
		when(projectService.updateProject(any(String.class), any(Project.class)))
				.thenReturn(updatedProject);
		
		//when
		mockMvc.perform(MockMvcRequestBuilders
				.put("/api/user/projects")
				.accept(MediaType.APPLICATION_JSON)
				.content(new Gson().toJson(updatedProject))
				.contentType(MediaType.APPLICATION_JSON)
				.principal(principal))
		
		//then
		.andDo(print())
		      .andExpect(
		    		 status().isOk())
		      .andExpect(
		    		  jsonPath("$.name", is(updatedProject.getName())));
		
	}
	
	@Test
	public void deleteProject_projectId_noContent() throws Exception {
		//given
		int projectId = 69;
		doNothing().when(projectService).deleteProject(mockUsername, projectId);
		
		//when
		mockMvc.perform(MockMvcRequestBuilders
				.delete("/api/user/projects/" + projectId)
				.principal(principal))
		
		//then
			.andExpect(status().isNoContent());
	}

	
}
