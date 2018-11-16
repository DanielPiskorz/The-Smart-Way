import { TestBed } from '@angular/core/testing';

import { ProjectsHttpService } from './projects-http.service';

describe('ProjectsHttpService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ProjectsHttpService = TestBed.get(ProjectsHttpService);
    expect(service).toBeTruthy();
  });
});
