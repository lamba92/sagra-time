import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ScaffoldComponent } from './scaffold.component';

describe('ScaffholdComponent', () => {
  let component: ScaffoldComponent;
  let fixture: ComponentFixture<ScaffoldComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ScaffoldComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ScaffoldComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
