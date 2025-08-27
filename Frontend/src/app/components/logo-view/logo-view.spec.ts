import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoView } from './logo-view';

describe('LogoView', () => {
  let component: LogoView;
  let fixture: ComponentFixture<LogoView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
