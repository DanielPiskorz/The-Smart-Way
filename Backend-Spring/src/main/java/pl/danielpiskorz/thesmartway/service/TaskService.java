package pl.danielpiskorz.thesmartway.service;

import pl.danielpiskorz.thesmartway.model.Task;

public interface TaskService {
	Task incrementTodoIndex(String username, long taskId);
	Task updateTodos(String username, Task task);
}
