import { Component, OnInit } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { OnBoarding } from '../on-boarding/on-boarding';
import { UiService } from '../../services/ui.service';
import { Router } from '@angular/router';
import { Message } from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { OnBoardingFooter } from '../../components/on-boarding-footer/on-boarding-footer';
import { OnBoardingHeading } from '../../components/on-boarding-heading/on-boarding-heading';
import { LogoView } from '../../components/logo-view/logo-view';
import { OnBoardingService } from '../../services/onBoarding.service';

@Component({
  selector: 'app-sign-up',
  imports: [ButtonModule, OnBoarding, FormsModule, ReactiveFormsModule, Message, InputTextModule, PasswordModule, OnBoardingFooter, OnBoardingHeading, LogoView],
  templateUrl: './sign-up.html',
  styleUrl: './sign-up.css'
})
export class SignUp implements OnInit {
  signUpForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private uiService: UiService,
    private router: Router,
    public onBoardingService: OnBoardingService
  ) {
    this.signUpForm = fb.group({
      fullName: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  ngOnInit(): void {
    this.onBoardingService.heading = 'Connect contracts with AI.'
    this.onBoardingService.subHeading = 'Fill out necessary details and Lets get started.';
  }

  get getFormControls() {
    return this.signUpForm?.controls;
  }

  onSubmit() {
    if (this.signUpForm?.invalid) {
      this.signUpForm.markAllAsTouched();
      return;
    }
  }
}
