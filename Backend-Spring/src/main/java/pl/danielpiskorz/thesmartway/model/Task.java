package pl.danielpiskorz.thesmartway.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tasks")
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column
	private String name;
	@ElementCollection
	@OrderColumn
	private List<String> todos = new ArrayList<>();
	
	@Column(nullable = false)
	private int currentTodoIndex = 0;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="project_id")
	@JsonIgnore
	private Project project;

	public Task() {}
	
	public Task(String name, List<String> todos, Project project) {
		super();
		this.name = name;
		this.todos = todos;
		this.project = project;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getTodos() {
		return todos;
	}

	public void setTodos(List<String> todos) {
		this.todos = todos;
	}
	
	public int getCurrentTodoIndex() {
		return currentTodoIndex;
	}
	
	public void setCurrentTodoIndex(int currentTodoIndex) {
		this.currentTodoIndex = currentTodoIndex;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	

}
