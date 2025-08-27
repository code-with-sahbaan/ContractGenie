import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OnBoardingFooter } from './on-boarding-footer';

describe('OnBoardingFooter', () => {
  let component: OnBoardingFooter;
  let fixture: ComponentFixture<OnBoardingFooter>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OnBoardingFooter]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OnBoardingFooter);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
