package pl.danielpiskorz.thesmartway.model;

public class Task {
	private long id;
	private String name;
	private String[] todos;

	public Task(long id, String name, String[] todos) {
		super();
		this.id = id;
		this.name = name;
		this.todos = todos;
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

	public String[] getTodos() {
		return todos;
	}

	public void setTodos(String[] todos) {
		this.todos = todos;
	}

}
