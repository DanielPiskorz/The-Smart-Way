import { Component, OnInit, Testability } from '@angular/core';
import { ProjectsHttpService } from '../services/projects-http.service';
import { Project, Task } from '../models/project';
import { faPlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-user-projects',
  templateUrl: './user-projects.component.html',
  styleUrls: ['./user-projects.component.css']
})
export class UserProjectsComponent implements OnInit {

  faPlus = faPlus;

  constructor(private projectsHttpService: ProjectsHttpService) { }

  projects: Array<Project>;
  currentTask: Task;

  newProjectName: string;

  ngOnInit() {
    this.getProjects();
  }

  selectTask(selectedTask: Task) {
    this.currentTask = selectedTask;
  }

  getProjects() {
    this.projectsHttpService.getProjects().subscribe(data => {
      this.projects = data;
     });
  }

  addProject() {
    const newProject: Project = new Project();
    newProject.name = this.newProjectName;
    this.projectsHttpService.createProject(newProject).subscribe(data => {
      this.projects.push(data);
      this.newProjectName = '';
    });
  }

  deleteProject(project: Project) {
    this.projectsHttpService.deleteProject(project);
  }


}
