import { Component, OnInit, Input, SimpleChanges, OnChanges } from '@angular/core';
import { TaskHttpService } from '../services/task-http.service';
import { Task } from '../models/project';

@Component({
  selector: 'app-current-task',
  templateUrl: './current-task.component.html',
  styleUrls: ['./current-task.component.css']
})
export class CurrentTaskComponent implements OnInit, OnChanges {


  constructor(private taskHttpService: TaskHttpService) {}

  @Input()
  currentTask: Task;

  todosSaved = true;
  savingTodos = false;

  synchronizedTodos: number;
  newTodoName;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.currentTask.previousValue !== changes.currentTask.currentValue) {
      this.synchronizedTodos = this.currentTask.todos.length;
    }
  }

  ngOnInit() {}




  done() {
    this.taskHttpService.todoDone(this.currentTask).subscribe(data => {
      console.log(data);
      this.update(data);
    });
  }


  saveTodos() {

    if (this.newTodoName.length > 0) {
      this.addNewTodo(this.newTodoName);
    }

    this.taskHttpService.updateTask(this.currentTask).subscribe(data => {
      this.update(data);
      this.synchronizedTodos = data.todos.length;
      this.todosSaved = true;
      this.savingTodos = false;

    });
  }

  addNewTodo(name) {
    if (name.length > 0) {
      this.todosSaved = false;
      this.currentTask.todos.push(name);
      this.newTodoName = '';
    }
  }

  update(task: Task) {
    if (this.currentTask.id === task.id) {
      this.currentTask.name = task.name;
      this.currentTask.todos = task.todos;
      this.currentTask.currentTodoIndex = task.currentTodoIndex;
    } else {
      console.error("Could't update task: id doesn't match.");
    }
  }

}
