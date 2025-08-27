import { Component } from '@angular/core';
import { OnBoardingService } from '../../services/onBoarding.service';

@Component({
  selector: 'app-on-boarding-heading',
  imports: [],
  templateUrl: './on-boarding-heading.html',
  styleUrl: './on-boarding-heading.css'
})
export class OnBoardingHeading {

  constructor(public onBoardingService:OnBoardingService){
    
  }

}
