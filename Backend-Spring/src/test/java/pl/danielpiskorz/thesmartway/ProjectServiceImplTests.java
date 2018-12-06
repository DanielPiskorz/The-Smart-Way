package pl.danielpiskorz.thesmartway;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
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
import pl.danielpiskorz.thesmartway.model.User;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;
import pl.danielpiskorz.thesmartway.repository.UserRepository;
import pl.danielpiskorz.thesmartway.service.ProjectServiceImpl;

@RunWith(SpringRunner.class)
public class ProjectServiceImplTests {

    @Mock
    ProjectRepository projectRepository;
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    ProjectServiceImpl projectService;
    
    User user;
	
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		
		user = new User("Josh", "joshee234", "josh567@gmail.com", "afdafdf543dfs" );
		user.setId(234L);
		when( userRepository.findByUsername(user.getName())).thenReturn(Optional.of(user));
		when(projectRepository.save(any(Project.class))).thenAnswer(new Answer<Project>() {
		    @Override
		    public Project answer(InvocationOnMock invocation) throws Throwable {
		        Project argProject = invocation.getArgument(0);
		        
		       while( !(argProject.getId() > 0) )
		    	   argProject.setId(1L + new Random().nextLong() * (Long.MAX_VALUE - 1));
		       
		    	return argProject;
		    }
		});
	}
	
	
	@Test
	public void createProject_validProject_matchingProject(){
		//given
		String projectName = "New Extra Project";
		
		//when
		Project createdProject = projectService.createProject(user.getName(), projectName);
		
		//then
		Assertions.assertThat(createdProject).isNotNull();
		Assertions.assertThat(createdProject.getId()).isGreaterThan(0);
		Assertions.assertThat(createdProject.getOwner()).isEqualTo(user);
		Assertions.assertThat(createdProject.getName()).isEqualTo(projectName);
	}
	
	@Test( expected = NullPointerException.class )
	public void createProject_nullProjectName_NullPointerException(){
		projectService.createProject(user.getName(), null);
	}
	
	@Test( expected = IllegalArgumentException.class )
	public void createProject_emptyProjectName_IllegalArgumentException(){
		projectService.createProject(user.getName(), "");
	}
	

}
