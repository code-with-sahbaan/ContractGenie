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

@Injectable({
  providedIn: 'root',
})
export class ContractService {

  constructor(private http: HttpClient) { }

  addContract(payload: FormData): Observable<any> {
    return this.http.post('/contract/v1/addContract', payload).pipe();
  }

  getContracts(payload: GetContracts): Observable<any> {
    return this.http.post('/contract/v1/getContracts', payload).pipe();
  }


}
