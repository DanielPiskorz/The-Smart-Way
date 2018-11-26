export class Project {
  id?: number;
  name?: string;
  tasks?: Array<Task>;
}

export class Task {
  id?: number;
  name?: string;
  todos?: string[] = [];
  currentTodoIndex?: number;
}
