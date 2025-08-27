import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
  HttpResponse,
  HttpErrorResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { getJWTtoken, logout } from '../utils/common.util';

@Injectable()
export class HttpConfigInterceptor implements HttpInterceptor {
  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    // 🔐 Add headers (e.g., auth token)
    const modifiedReq = environment.nonTokenizedURL.has(req.url)
      ? req.clone({
          url: `${environment.apiUrl}${req.url}`,
        })
      : req.clone({
          setHeaders: {
            Authorization: `Bearer ${getJWTtoken()}`,
          },
          url: `${environment.apiUrl}${req.url}`,
        });
    return next.handle(modifiedReq).pipe(
      tap({
        next: (event) => {
          if (event instanceof HttpResponse) {
            // ✅ Handle responses globally
          }
        },
        error: (error: HttpErrorResponse) => {
          // If token expires or never logged in
          if (error.status === 403) {
            logout();
          }
        },
      })
    );
  }
}
