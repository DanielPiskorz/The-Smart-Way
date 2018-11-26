import { Component, OnInit, Input } from '@angular/core';
import { TaskHttpService } from '../services/task-http.service';

@Component({
  selector: 'app-current-task',
  templateUrl: './current-task.component.html',
  styleUrls: ['./current-task.component.css']
})
export class CurrentTaskComponent implements OnInit {


  constructor(private taskHttpService: TaskHttpService) { }

  @Input()
  currentTask;

  todosSaved = true;
  savingTodos = false;

  synchronizedTodos: number;
  newTodoName;

  ngOnInit() {
  }

  done() {
    this.taskHttpService.todoDone(this.currentTask).subscribe(data => {
      console.log(data);
      this.currentTask = data;
    });
  }


  saveTodos() {
    this.taskHttpService.updateTask(this.currentTask).subscribe(data => {
      this.currentTask = data;
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


}
