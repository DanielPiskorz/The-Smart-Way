import { Component, OnInit, Testability } from '@angular/core';
import { ProjectsHttpService } from '../services/projects-http.service';
import { Project, Task } from '../models/project';
import { faPlus, faPencilAlt, faTimes, faSave, faTasks } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-user-projects',
  templateUrl: './user-projects.component.html',
  styleUrls: ['./user-projects.component.css']
})
export class UserProjectsComponent implements OnInit {

  faPlus = faPlus;
  faTimes = faTimes;
  faPencilAlt = faPencilAlt;
  faSave = faSave;

  constructor(private projectsHttpService: ProjectsHttpService) { }

  projects: Array<Project>;
  currentTask = new Task;

  newProjectName: string;
  mouseOnProjectId: number;
  mouseOnProjectHeaderId: number;
  editingProject = new Project();
  editingTask = new Task();
  showNewTaskButton = true;
  newTodoName = '';

  todosSaved = true;
  savingTodos = false;


  ngOnInit() {
    this.getProjects();
  }


  changeCurrentTask(task: Task) {
    this.currentTask = task;
  }


  getProjects() {
    this.projectsHttpService.getProjects().subscribe(data => {
      this.projects = data;
     });
  }

  addProject() {
    if (this.newProjectName.length > 0) {
      const newProject: Project = new Project();
      newProject.name = this.newProjectName;
      this.projectsHttpService.createProject(newProject).subscribe(data => {
        this.projects.push(data);
        this.newProjectName = '';
      });
    }

  }

  finishCurrentTask() {
    this.removeTask(this.currentTask);
  }

  removeTask(task: Task) {
    const project: Project = this.projects.filter(p => p.tasks.includes(task))[0];
    if (project) {
      project.tasks.splice(project.tasks.indexOf(task), 1);
      this.projectsHttpService.updateProject(project).subscribe(data => {
        this.projects.splice(this.projects.indexOf(project), 1, data);
        this.currentTask = new Task;
      });
    }
  }

}
