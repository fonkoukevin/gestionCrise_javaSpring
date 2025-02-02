import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouvelleCriseComponent } from './nouvelle-crise.component';

describe('NouvelleCriseComponent', () => {
  let component: NouvelleCriseComponent;
  let fixture: ComponentFixture<NouvelleCriseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouvelleCriseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouvelleCriseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
