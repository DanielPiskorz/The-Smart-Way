import { Component, OnInit, Testability } from '@angular/core';
import { ProjectsHttpService } from '../services/projects-http.service';
import { Project, Task } from '../models/project';
import { faPlus, faPencilAlt, faTimes } from '@fortawesome/free-solid-svg-icons';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-user-projects',
  templateUrl: './user-projects.component.html',
  styleUrls: ['./user-projects.component.css'],
  animations: [
    trigger('projectOptions', [
      state('void', style({
        left: 0,
        opacity: 0
      })),
      transition('void <=> *', [
        animate(100)
      ])
    ])
  ]
})
export class UserProjectsComponent implements OnInit {

  faPlus = faPlus;
  faTimes = faTimes;
  faPencilAlt = faPencilAlt;

  constructor(private projectsHttpService: ProjectsHttpService) { }

  projects: Array<Project>;
  currentTask: Task;

  newProjectName: string;
  mouseOnProjectId: number;

  ngOnInit() {
    this.getProjects();
  }

  // LOCAL start

  selectTask(selectedTask: Task) {
    this.currentTask = selectedTask;
  }

  projectHeaderMouseAction(projectId: number, action: String) {
    switch (action) {
      case 'enter':
        this.mouseOnProjectId = projectId;
        break;
      case 'leave':
        this.mouseOnProjectId = -1;
        break;
    }

  }


  // LOCAL end
  // HTTP SERVICE start

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
    this.projectsHttpService.deleteProject(project).subscribe(
      () => this.projects.splice(this.projects.indexOf(project), 1)
    );
  }

  // HTTP SERVICE end
}
