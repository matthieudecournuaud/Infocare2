import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { olsenGuard } from './olsen.guard';

describe('olsenGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => TestBed.runInInjectionContext(() => olsenGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
