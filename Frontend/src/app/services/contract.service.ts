// src/app/core/services/ui.service.ts
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

export interface GetContracts{
    folderId: number
}

export interface ContractList{
    contractId: number,
    contractName: string,
    folderId: number
}

export interface FolderList{
    folderId: number,
    folderName: string,
    contracts: ContractList[]
}

export interface AddContract{
  contractName: string,
  contractUrl: string,
  folderId: number,
  contractFileName: string
}

export interface UpdateContract{
  contractName: string,
  contractUrl: string,
  folderId: number,
  contractFileName: string
  contractId: number
}

export interface UploadFileResponse{
  fileName: string,
  fileUrl: string
}

export interface DeleteContract{
  contractId: number,
  folderId: number
}

@Injectable({
  providedIn: 'root',
})
export class ContractService {

  constructor(private http: HttpClient) { }

  addContract(payload: AddContract): Observable<any> {
    return this.http.post('/contract/v1/addContract', payload).pipe();
  }

  updateContract(payload: AddContract): Observable<any> {
    return this.http.post('/contract/v1/updateContract', payload).pipe();
  }

  deleteContract(payload: DeleteContract): Observable<any> {
    return this.http.post('/contract/v1/deleteContract', payload).pipe();
  }

  getContracts(payload: GetContracts): Observable<any> {
    return this.http.post('/contract/v1/getContracts', payload).pipe();
  }

  uploadFile(payload: FormData): Observable<any>{
    return this.http.post('/user/v1/uploadFile', payload).pipe();
  }


}
