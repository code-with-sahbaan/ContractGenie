import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OnBoardingHeading } from './on-boarding-heading';

describe('OnBoardingHeading', () => {
  let component: OnBoardingHeading;
  let fixture: ComponentFixture<OnBoardingHeading>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OnBoardingHeading]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OnBoardingHeading);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
