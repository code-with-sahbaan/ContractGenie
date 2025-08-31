import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { Dialog } from 'primeng/dialog';
import { ForgotPassword, OnBoardingService, VerifyOTPRequest } from '../../services/onBoarding.service';
import { StepperModule } from 'primeng/stepper';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Message } from 'primeng/message';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { finalize } from 'rxjs';
import { UiService } from '../../services/ui.service';
import { SkeletonModule } from 'primeng/skeleton';

@Component({
  selector: 'app-on-boarding',
  imports: [Dialog, ButtonModule, StepperModule, Message, InputTextModule, FormsModule, ReactiveFormsModule, PasswordModule, SkeletonModule],
  templateUrl: './on-boarding.html',
  styleUrl: './on-boarding.css'
})
export class OnBoarding {

  forgotPasswordForm: FormGroup;
  isLoading: boolean = false;
  verifyProfileForm: FormGroup;

  constructor(public onBoardingService: OnBoardingService, public formBuilder: FormBuilder, public uiService: UiService) {
    this.forgotPasswordForm = formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      otp: ['', [Validators.required, Validators.minLength(5)]]
    });

    
    this.verifyProfileForm = formBuilder.group({
      otp: ['', [Validators.required, Validators.minLength(5)]]
    })
  }

  get getFormControls() {
    return this.forgotPasswordForm?.controls;
  }

  get getVerifyFormControls() {
    return this.verifyProfileForm.controls;
  }

  resetForgotPassword(stepper: any) {
    this.forgotPasswordForm.reset();
    alert('Submit');
    stepper.value.set(1);
    this.onBoardingService.forgotPassword = false;
  }

  sendOTP(activateCallback: Function) {
    if (this.forgotPasswordForm.get('email')?.value == null) {
      return;
    }
    const payload: ForgotPassword = {
      email: this.forgotPasswordForm.get('email')?.value
    }
    this.isLoading = true;
    this.onBoardingService
      .sendForgotPasswordOtp(payload)
      .pipe(finalize(() => {
        this.isLoading = false;
      }))
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("OTP Send successfully");
          activateCallback(2);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  onSubmit(stepper: any) {
    if (this.forgotPasswordForm?.invalid) {
      this.forgotPasswordForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    const payload: VerifyOTPRequest = {
      email: this.forgotPasswordForm.get('email')?.value,
      otp: this.forgotPasswordForm.get('otp')?.value,
      verificationType: "forgotPassword",
      password: this.forgotPasswordForm.get('password')?.value
    };
    this.onBoardingService
      .verifyOtp(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.isLoading = false;
          this.resetForgotPassword(stepper);
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("OTP verified successfully");
          this.onBoardingService.forgotPassword = false;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  verifyProfile() {
    if (this.verifyProfileForm?.invalid) {
      this.verifyProfileForm.markAllAsTouched();
      return;
    }
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    const payload: VerifyOTPRequest = {
      email: this.onBoardingService.email,
      otp: this.verifyProfileForm.get('otp')?.value,
      verificationType: "profileActivation",
      password: ""
    };
    this.onBoardingService
      .verifyOtp(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("OTP verified successfully");
          this.onBoardingService.verifyProfileModal = false;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}
