package pl.danielpiskorz.thesmartway;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import pl.danielpiskorz.thesmartway.model.User;
import pl.danielpiskorz.thesmartway.repository.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

	@Autowired
	UserRepository userRepository;
	
	@Test
	public void saveAndFindByUsername(){
		//given
		User givenUser = new User("George", "george123", "happyGeorge123@gmail.com", "ilikeflowers123");
		User savedUser = userRepository.save(givenUser);
		
		//when
		User foundUser = userRepository.findByUsername("george123").get();
		
		//then
		Assertions.assertThat(foundUser).isNotNull();
		Assertions.assertThat(foundUser.getId()).isGreaterThan(0);
		Assertions.assertThat(savedUser.getUsername()).isEqualTo(givenUser.getUsername());
		Assertions.assertThat(foundUser).isEqualTo(savedUser);
		
	}
}
