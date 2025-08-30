import { Component } from '@angular/core';
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
import { OnBoardingService, SignInRequest, VerifyOTPRequest } from '../../services/onBoarding.service';
import { finalize } from 'rxjs';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-sign-in',
  imports: [
    ButtonModule,
    OnBoarding,
    FormsModule,
    ReactiveFormsModule,
    Message,
    InputTextModule,
    PasswordModule,
    OnBoardingFooter,
    OnBoardingHeading,
    LogoView,
    DialogModule,

  ],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.css'
})
export class SignIn {

  signInForm: FormGroup;
  verifyProfileForm: FormGroup;
  verifyProfileVisible: boolean = false;

  constructor(
    private fb: FormBuilder,
    private uiService: UiService,
    private router: Router,
    public onBoardingService: OnBoardingService
  ) {
    this.signInForm = fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });

    this.verifyProfileForm = fb.group({
      otp: ['', [Validators.required, Validators.minLength(5)]]
    })
  }

  get getFormControls() {
    return this.signInForm?.controls;
  }

  get getVerifyFormControls() {
    return this.verifyProfileForm.controls;
  }

  onSubmit() {
    if (this.signInForm?.invalid) {
      this.signInForm.markAllAsTouched();
      return;
    }
    /**
     * Showing Loader
     */
    this.uiService.showSpinner();
    /**
     * Calling API
     */
    const payload: SignInRequest = {
      email: this.signInForm.get('email')?.value,
      password: this.signInForm.get('password')?.value
    }
    this.onBoardingService
      .signIn(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
          const user = response.responseBody;
          if (user.isActive == false) {
            this.verifyProfileVisible = true;
          } else {
            localStorage.setItem("USER", JSON.stringify(user));
          }
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
      email: this.signInForm.get('email')?.value,
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
          this.verifyProfileVisible = false;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

}
