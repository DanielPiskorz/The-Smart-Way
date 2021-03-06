package pl.danielpiskorz.thesmartway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import pl.danielpiskorz.thesmartway.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	Project findById(long id);
	List<Project> findAllProjectsByOwnerUsername(String username);
	<S extends Project> S save(S project);
	void deleteById(long id);
}