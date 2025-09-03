// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface AddFolder{
    folderName: string
}

export interface UpdateFolder{
    folderName: string,
    folderId: number
}

export interface DeleteFolder{
  folderId: number
}

@Injectable({
  providedIn: 'root',
})
export class FolderService {

  constructor(private http: HttpClient) { }

  addFolder(payload: AddFolder): Observable<any> {
    return this.http.post('/folder/v1/addFolder', payload).pipe();
  }

  updateFolder(payload: UpdateFolder): Observable<any> {
    return this.http.post('/folder/v1/updateFolder', payload).pipe();
  }

  deleteFolder(payload: DeleteFolder): Observable<any> {
    return this.http.post('/folder/v1/deleteFolder', payload).pipe();
  }

  getFolders(): Observable<any> {
    return this.http.get('/folder/v1/getFolders').pipe();
  }

}
