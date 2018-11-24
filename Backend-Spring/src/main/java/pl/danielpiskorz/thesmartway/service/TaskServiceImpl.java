package pl.danielpiskorz.thesmartway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.danielpiskorz.thesmartway.model.Task;
import pl.danielpiskorz.thesmartway.repository.ProjectRepository;
import pl.danielpiskorz.thesmartway.repository.TaskRepository;

@Service
public class TaskServiceImpl implements TaskService{

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	TaskRepository taskRepository;
	
	
	@Override
	public Task incrementTodoIndex(String username, int taskId) {
		Task task = taskRepository.findById(taskId);
		
		if(!task.getProject().getOwner().getUsername().equals(username))
			throw new SecurityException("User doesn't own the task");
		
		int newIndex = task.getCurrentTodoIndex() + 1;
		
		if((newIndex) > task.getTodos().size())
			throw new IndexOutOfBoundsException("Current index is already the last.");
		
		task.setCurrentTodoIndex(newIndex);
		return taskRepository.save(task);
		
	}


	@Override
	public Task updateTodos(String username, Task task) {
		Task taskToSave = taskRepository.findById(task.getId());
		
		
		
		if(!taskToSave.getProject().getOwner().getUsername().equals(username))
			throw new SecurityException("User doesn't own the task");
		
		taskToSave.setTodos(task.getTodos());
		
		return taskRepository.save(taskToSave);
		
	}

	
}
