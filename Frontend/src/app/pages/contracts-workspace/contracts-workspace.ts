import { Component } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { Chip } from 'primeng/chip';
import { TabsModule } from 'primeng/tabs';

@Component({
  selector: 'app-contracts-workspace',
  imports: [InputTextModule, ButtonModule, AccordionModule, Chip, TabsModule],
  templateUrl: './contracts-workspace.html',
  styleUrl: './contracts-workspace.css'
})
export class ContractsWorkspace {

  activeContractId: number = 1;
  activeFolderId: number = 1;
  folders: any[] = [
    {
      folderId: 1,
      folderName: 'Asia Pacific Contracts',
      contracts: [
        {
          contractId: 1,
          contractName: 'Malaysian University'
        },
        {
          contractId: 2,
          contractName: 'Indonesian University'
        },
        {
          contractId: 3,
          contractName: 'Singaporian University'
        }
      ]
    },
    {
      folderId: 2,
      folderName: 'European Contracts',
      contracts: [
        {
          contractId: 4,
          contractName: 'German University'
        },
        {
          contractId: 5,
          contractName: 'Italian University'
        },
        {
          contractId: 6,
          contractName: 'Ethopian University'
        }
      ]
    },
  ]

  activateContract(contractId:number){
    this.activeContractId = contractId;
  }

  activateFolder(folderId:number){
    this.activeFolderId = folderId;
  }

  tabChange(index: any){
    this.activeContractId = index;
  }
}
