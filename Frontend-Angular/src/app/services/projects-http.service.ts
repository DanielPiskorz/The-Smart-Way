import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Project } from '../models/project';
import { TokenStorageService } from './token-storage.service';
import { Task } from '../models/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectsHttpService {

  PROJECT_URL = 'http://localhost:8080/api/user/projects';
  TASK_URL = 'http://localhost:8080/api/user/tasks';

  constructor(private http: HttpClient, private tokenStorage: TokenStorageService) { }

  getProjects(): Observable<Array<Project>> {
    return this.http.get<Array<Project>>(this.PROJECT_URL);
  }

  createProject(project: Project): Observable<Project> {
    return this.http.post<Project>(this.PROJECT_URL, project);
  }

  updateProject(project: Project): Observable<Project> {
    return this.http.put<Project>(this.PROJECT_URL, project);
  }

  deleteProject(project) {
    return this.http.delete(this.PROJECT_URL + '/' + project.id);
  }

  todoDone(task: Task): Observable<Task> {
    return this.http.patch(`${this.TASK_URL}/${task.id}/done`, null);
  }

  updateTask(task: Task): Observable<Task> {
    return this.http.patch(`${this.TASK_URL}/${task.id}`, task);
  }
}
