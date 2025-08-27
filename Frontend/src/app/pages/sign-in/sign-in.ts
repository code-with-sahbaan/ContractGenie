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

@Component({
  selector: 'app-sign-in',
  imports: [ButtonModule, OnBoarding, FormsModule, ReactiveFormsModule, Message, InputTextModule, PasswordModule],
  templateUrl: './sign-in.html',
  styleUrl: './sign-in.css'
})
export class SignIn {

  
  signInForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private uiService: UiService,
    private router: Router,
  ) {
    this.signInForm = fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  get getFormControls() {
    return this.signInForm?.controls;
  }

  onSubmit() {
    if (this.signInForm?.invalid) {
      this.signInForm.markAllAsTouched();
      return;
    }
  }

}
