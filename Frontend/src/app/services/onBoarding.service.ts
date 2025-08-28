// src/app/core/services/ui.service.ts
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class OnBoardingService {
    
    public heading:string = 'Glad To See You Back';
    public subHeading:string = 'Enter your email and password to continue.';
    public forgotPassword:boolean = false;
}
