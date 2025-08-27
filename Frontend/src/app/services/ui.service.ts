// src/app/core/services/ui.service.ts
import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root',
})
export class UiService {
  isLoading = false;
  constructor(private messageService: MessageService) {}

  showSpinner() {
    this.isLoading = true;
  }

  hideSpinner() {
    this.isLoading = false;
  }

  showSuccess(detail: string) {
    this.messageService.add({
      severity: 'success',
      summary: 'Request Process Successfully',
      detail: detail,
      life: 3000,
    });
  }

  showError(detail: string) {
    this.messageService.add({
      severity: 'error',
      summary: 'Something went wrong',
      detail: detail,
      life: 3000,
    });
  }
}
