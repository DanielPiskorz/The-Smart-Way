import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Project, Task } from '../models/project';
import { ProjectsHttpService } from '../services/projects-http.service';
import { faPlus, faPencilAlt, faTimes, faSave, faTasks } from '@fortawesome/free-solid-svg-icons';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css'],
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
export class ProjectComponent implements OnInit {

  faPlus = faPlus;
  faTimes = faTimes;
  faPencilAlt = faPencilAlt;
  faSave = faSave;

  constructor(private projectsHttpService: ProjectsHttpService) { }

  @Input()
  project: Project;

  @Output()
  selectedTask = new EventEmitter<Task>();
  selectedTaskLocal = new Task();

  mouseOnProject = false;
  mouseOnProjectHeader = false;
  headerEditing = false;
  editingTask = new Task();
  showNewTaskButton = true;

  ngOnInit() {
  }

  projectMouseAction(action: string) {
    switch (action) {
      case 'enter':
        this.mouseOnProject = true;
        break;
      case 'leave':
        this.mouseOnProject = false;
        break;
    }
  }

  projectHeaderMouseAction(action: string) {
    switch (action) {
      case 'enter':
        this.mouseOnProjectHeader = true;
        break;
      case 'leave':
        this.mouseOnProjectHeader = false;
        break;
    }

  }

  editProjectHeader() {
    this.headerEditing = true;
  }

  showNewTaskInput() {
    this.showNewTaskButton = false;
    const newTask = new Task();
    this.project.tasks.push(newTask);
    this.editingTask = newTask;
  }

  selectTask(selectedTask: Task) {
    this.selectedTask.emit(selectedTask);
    this.selectedTaskLocal = selectedTask;
  }

  editTaskName(task: Task) {
    this.showNewTaskButton = false;
    this.editingTask = task;
  }

  saveProjectHeader() {
    this.projectsHttpService.updateProject(this.project).subscribe(data => {
      this.update(data);
      this.headerEditing = false;
    });
  }

  saveTaskName() {
    if (this.editingTask.name.length > 0) {


      this.projectsHttpService.updateProject(this.project).subscribe(data => {
       this.update(data);
        this.editingTask = new Task();
      });

      this.showNewTaskButton = true;
    }
  }

  deleteProject() {
    this.projectsHttpService.deleteProject(this.project).subscribe(
      () => this.project = new Project()
    );
  }

  public update(project: Project) {
    if (this.project.id === project.id) {
      this.project.name = project.name;
      this.project.tasks = project.tasks;
    } else {
      console.error("Could't update project: id doesn't match.");
    }
  }
}
