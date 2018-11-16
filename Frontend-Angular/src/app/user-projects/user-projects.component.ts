import { Component, OnInit, Testability } from '@angular/core';
import { ProjectsHttpService } from '../services/projects-http.service';
import { Project } from '../models/project';

@Component({
  selector: 'app-user-projects',
  templateUrl: './user-projects.component.html',
  styleUrls: ['./user-projects.component.css']
})
export class UserProjectsComponent implements OnInit {

  constructor(private projectsHttpService: ProjectsHttpService) { }

  projects: Array<Project>;

  ngOnInit() {
  }

  getProjects() {
    this.projectsHttpService.getProjects().subscribe(data => {
      this.projects = data;
     });
  }


}
