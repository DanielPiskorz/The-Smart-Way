import { TestBed } from '@angular/core/testing';

import { TaskHttpService } from './task-http.service';

describe('TaskHttpService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: TaskHttpService = TestBed.get(TaskHttpService);
    expect(service).toBeTruthy();
  });
});
