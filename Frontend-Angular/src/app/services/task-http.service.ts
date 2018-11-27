import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Task } from '../models/project';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TaskHttpService {

  TASK_URL = 'http://localhost:8080/api/user/tasks';

  constructor(private http: HttpClient) { }

  todoDone(task: Task): Observable<Task> {
    return this.http.patch<Task>(`${this.TASK_URL}/${task.id}/done`, null);
  }

  updateTask(task: Task): Observable<Task> {
    return this.http.patch<Task>(`${this.TASK_URL}/${task.id}`, task);
  }
}
