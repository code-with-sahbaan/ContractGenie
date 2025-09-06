// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface ChatPrompt {
  userMessage: string,
  aiMessage: string
}

export interface AskAI{
  userPrompt: string
}

@Injectable({
  providedIn: 'root',
})
export class AiService {

  constructor(private http: HttpClient) { }


  getAiAnswer(payload: AskAI): Observable<any> {
    return this.http.post('/contract/v1/getAiAnswer', payload).pipe();
  }
}
