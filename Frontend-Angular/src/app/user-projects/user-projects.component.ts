import { Component, OnInit, Testability } from '@angular/core';
import { ProjectsHttpService } from '../services/projects-http.service';
import { Project, Task } from '../models/project';
import { faPlus, faPencilAlt, faTimes, faSave } from '@fortawesome/free-solid-svg-icons';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-user-projects',
  templateUrl: './user-projects.component.html',
  styleUrls: ['./user-projects.component.css'],
  animations: [
    trigger('projectOptions', [
      state('void', style({
        left: 0,
        opacity: 0,
        fontSize: 1,
        top: 17.5
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
  faSave = faSave;

  constructor(private projectsHttpService: ProjectsHttpService) { }

  projects: Array<Project>;
  currentTask: Task;

  newProjectName: string;
  mouseOnProjectId: number;
  editingProject = new Project();

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

  editProjectHeader(project: Project) {
    this.editingProject = project;
  }




  // LOCAL end
  // HTTP SERVICE start

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

  saveProjectHeader(project: Project) {
    this.projectsHttpService.updateProject(project).subscribe(data => {
      this.projects[this.projects.indexOf(project)] = data;
      this.editingProject = new Project();
    });
  }

  deleteProject(project: Project) {
    this.projectsHttpService.deleteProject(project).subscribe(
      () => this.projects.splice(this.projects.indexOf(project), 1)
    );
  }

  // HTTP SERVICE end
}
