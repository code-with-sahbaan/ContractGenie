// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface SignInRequest {
  email: string,
  password: string
}

export interface VerifyOTPRequest {
  email: string,
  otp: string,
  verificationType: string,
  password: string
}

@Injectable({
  providedIn: 'root',
})
export class OnBoardingService {

  constructor(private http: HttpClient) { }

  public heading: string = 'Glad To See You Back';
  public subHeading: string = 'Enter your email and password to continue.';
  public forgotPassword: boolean = false;

  signIn(payload: SignInRequest): Observable<any> {
    return this.http.post('/user/login', payload).pipe();
  }

  verifyOtp(payload: VerifyOTPRequest): Observable<any> {
    return this.http.post('/user/v1/verifyOtp', payload).pipe();
  }
}
