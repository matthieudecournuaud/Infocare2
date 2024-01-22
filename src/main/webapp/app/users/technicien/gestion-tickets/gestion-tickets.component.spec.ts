import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionTicketsComponent } from './gestion-tickets.component';

describe('GestionTicketsComponent', () => {
  let component: GestionTicketsComponent;
  let fixture: ComponentFixture<GestionTicketsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionTicketsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(GestionTicketsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
