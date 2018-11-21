import { Component, OnInit, Testability } from '@angular/core';
import { ProjectsHttpService } from '../services/projects-http.service';
import { Project, Task } from '../models/project';
import { faPlus, faPencilAlt, faTimes, faSave, faTasks } from '@fortawesome/free-solid-svg-icons';
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
    ]),
    trigger('newTaskButton', [
      state('void', style({
        opacity: 0,
        fontSize: 0,
        bottom: -18,
        left: 10
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

  // LOCAL start

  selectTask(selectedTask: Task) {
    this.currentTask = selectedTask;
  }

  projectMouseAction(projectId, action: string) {
    switch (action) {
      case 'enter':
        this.mouseOnProjectId = projectId;
        break;
      case 'leave':
        this.mouseOnProjectId = -1;
        break;
    }
  }

  projectHeaderMouseAction(projectId: number, action: string) {
    switch (action) {
      case 'enter':
        this.mouseOnProjectHeaderId = projectId;
        break;
      case 'leave':
        this.mouseOnProjectHeaderId = -1;
        break;
    }

  }

  editProjectHeader(project: Project) {
    this.editingProject = project;
  }

  showNewTaskInput(project: Project) {
    this.showNewTaskButton = false;
    const newTask = new Task();
    project.tasks.push(newTask);
    this.editingTask = newTask;
  }

  editTaskName(task: Task) {
    this.showNewTaskButton = false;
    this.editingTask = task;
  }

  addNewTodo(name) {
    if (name.length > 0) {
      this.todosSaved = false;
      this.currentTask.todos.push(name);
      this.newTodoName = '';
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

  saveTaskName() {
    if (this.editingTask.name.length > 0) {
      let project: Project;

      this.projects.filter(p => {
        p.tasks.filter(t => {
          if (t === this.editingTask) {
            project = p;
          }
        });
      });

      this.projectsHttpService.updateProject(project).subscribe(data => {
        this.projects[this.projects.indexOf(project)] = data;
        this.editingTask = new Task();
      });

      this.showNewTaskButton = true;
    }
  }

  deleteProject(project: Project) {
    this.projectsHttpService.deleteProject(project).subscribe(
      () => this.projects.splice(this.projects.indexOf(project), 1)
    );
  }

  saveTodos() {
    this.savingTodos = true;
    let project: Project;
    this.projects.filter(p => {
      p.tasks.filter( t => {
        if (t === this.currentTask) {
          project = p;
        }
       });
    });

    this.projectsHttpService.updateProject(project).subscribe(data => {
      project = data;
      this.todosSaved = true;
      this.savingTodos = false;

    });
  }

  // HTTP SERVICE end
}
