package pl.danielpiskorz.thesmartway.service;

import pl.danielpiskorz.thesmartway.model.Task;

public interface TaskService {
	Task incrementTodoIndex(String username, int taskId);
	Task updateTodos(String username, Task task);
}
