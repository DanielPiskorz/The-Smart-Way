package pl.danielpiskorz.thesmartway;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.google.gson.Gson;

import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.model.forms.SignUpForm;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;
import pl.danielpiskorz.thesmartway.repository.TaskRepository;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-integrationtest.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class IntegrationTests {
 
    @Autowired
    MockMvc mvc;

    @Autowired
    ProjectRepository projectRepository;
    
    @Autowired
    TaskRepository taskRepository;
    
    final String mockUsername = "gooduserbob56";
    final String mockPassword = "veryHard23";
    
    private static String jwtToken;
    
	private String[] projectNames = new String[]{
			"Integration tests!",
			"Let's combine everything!"};
	
    
    @Test
    @Sql(scripts = "classpath:role_schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void step1_signUp() throws Exception{
    	//given
    	SignUpForm mockUserSignup = new SignUpForm();
    	mockUserSignup.setName("Bob xd");
    	mockUserSignup.setUsername(mockUsername);
    	mockUserSignup.setPassword(mockPassword);
    	mockUserSignup.setEmail("uncl543eBob@xsdad.com");
    	Set<String> roles = new HashSet<>();
    	roles.add("USER");
    	mockUserSignup.setRole(roles);    	
    	String signupJson = new Gson().toJson(mockUserSignup);
    	
    	//when
    	mvc.perform(post("/api/auth/signup")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(signupJson))
    	//then
    		.andExpect(
    				status().isOk());

  
    }
    
    @Test
    public void step2_signIn() throws Exception{
    	SignUpForm mockUserSignin = new SignUpForm();
    	mockUserSignin.setUsername(mockUsername);
    	mockUserSignin.setPassword(mockPassword); 	
    	String signinJson = new Gson().toJson(mockUserSignin);
    	
    	MvcResult result = mvc.perform(post("/api/auth/signin")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(signinJson))
    	
    		.andExpect(
    				status().isOk())
    		.andExpect(
    				jsonPath("$.tokenType", is("Bearer")))
    		.andExpect(
    				jsonPath("$.accessToken", allOf(
    						notNullValue(),
    						isA(String.class),
    						not(isEmptyString()))))
    		.andExpect(
    				jsonPath("$.username", is(mockUsername)))
    		
    		.andDo(
    				print())
    		.andReturn();
    	
    	//assign jwt to use below
    	JSONObject response = new JSONObject(result.getResponse().getContentAsString());
    	System.out.println(response);
    	jwtToken = String.format("%s %s",
    			response.getString("tokenType"),
    			response.getString("accessToken"));
    	
    	Assertions.assertThat(jwtToken).isNotEmpty().isNotNull();
    	
    }
    
    @Test
    public void step3_createProjects() throws Exception {
    	//given
		for(String projectName : projectNames){
			
			//when
			 mvc.perform(post("/api/user/projects")
		        		.contentType(MediaType.APPLICATION_JSON)
		        		.content(String.format("{\"name\": \"%s\"}", projectName))
		        		.header("Authorization", jwtToken))
			 
			 //then
		          .andExpect(
		        		  status().isOk())
		          .andExpect(
		        		  content()
		        		  .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		          .andExpect(
		        		  jsonPath("$.name", is(projectName)));
		}
    }

    @Test
    public void step4_getAllProjects() throws Exception {
    	//given
    		//projects above (saved to mock db in step3)
    	
    	//when
    	mvc.perform(get("/api/user/projects")
    			.header("Authorization", jwtToken))
    	
    	//then
    		.andExpect(
    				status().isOk())
    		.andExpect(
    				content()
    				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    		.andExpect(
    				jsonPath("$", hasSize(projectNames.length)))
    		.andExpect(
    				jsonPath("$[1].name", is(projectNames[1])));
    	
    }
    
    
    
}