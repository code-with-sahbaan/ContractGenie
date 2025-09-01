// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ChatPrompt{
    userMessage: string,
    aiMessage: string
}

@Injectable({
  providedIn: 'root',
})
export class AiService {

  constructor(private http: HttpClient) { }


}
