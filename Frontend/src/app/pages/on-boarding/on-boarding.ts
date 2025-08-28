import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { OnBoardingService } from '../../services/onBoarding.service';
import { StepperModule } from 'primeng/stepper';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Message } from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
@Component({
  selector: 'app-on-boarding',
  imports: [Dialog, ButtonModule, StepperModule, Message, InputTextModule, FormsModule, ReactiveFormsModule, PasswordModule],
  templateUrl: './on-boarding.html',
  styleUrl: './on-boarding.css'
})
export class OnBoarding {

  forgotPasswordForm: FormGroup;

  constructor(public onBoardingService: OnBoardingService, public formBuilder: FormBuilder) {
    this.forgotPasswordForm = formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      otp: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get getFormControls() {
    return this.forgotPasswordForm?.controls;
  }

  resetForgotPassword(stepper: any) {
    this.forgotPasswordForm.reset();
    alert('Submit');
    stepper.value.set(1);
    this.onBoardingService.forgotPassword = false;
  }

  onSubmit(stepper: any) {
    if (this.forgotPasswordForm?.invalid) {
      this.forgotPasswordForm.markAllAsTouched();
      return;
    }
    this.resetForgotPassword(stepper);
  }
}
