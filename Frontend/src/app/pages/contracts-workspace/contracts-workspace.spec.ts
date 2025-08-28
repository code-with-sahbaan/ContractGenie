import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContractsWorkspace } from './contracts-workspace';

describe('ContractsWorkspace', () => {
  let component: ContractsWorkspace;
  let fixture: ComponentFixture<ContractsWorkspace>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContractsWorkspace]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContractsWorkspace);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
