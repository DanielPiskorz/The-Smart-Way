package pl.danielpiskorz.thesmartway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.danielpiskorz.thesmartway.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
	Task findById(long taskId);
	<S extends Task> S save(S task);

}