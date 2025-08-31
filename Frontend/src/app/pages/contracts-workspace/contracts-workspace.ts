import { Component, HostListener, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { TabList, TabsModule } from 'primeng/tabs';
import { DrawerModule } from 'primeng/drawer';
import { FormsModule } from '@angular/forms';
import { Dialog } from 'primeng/dialog';

@Component({
  selector: 'app-contracts-workspace',
  imports: [InputTextModule, ButtonModule, AccordionModule, TabsModule, DrawerModule, FormsModule, Dialog],
  templateUrl: './contracts-workspace.html',
  styleUrl: './contracts-workspace.css'
})
export class ContractsWorkspace {

  @ViewChild('tablistRef') tabList!: TabList;
  activeContractId: number = 0;
  activeFolderId: number = 0;
  selectedContracts: Map<number, any> = new Map<number, any>();
  isDesktop = true;
  visibleContract = false;
  query: string = "";
  queryList: string[] = [];
  addFolderModal: boolean = false;
  folderName: string = '';

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }
  folders: any[] = [
    {
      folderId: 1,
      folderName: 'Asia Pacific Contracts',
      contracts: [
        {
          contractId: 1,
          contractName: 'Malaysian University',
          folderId: 1
        },
        {
          contractId: 2,
          contractName: 'Indonesian University',
          folderId: 1
        },
        {
          contractId: 3,
          contractName: 'Singaporian University',
          folderId: 1
        }
      ]
    },
    {
      folderId: 2,
      folderName: 'European Contracts',
      contracts: [
        {
          contractId: 4,
          contractName: 'German University',
          folderId: 2
        },
        {
          contractId: 5,
          contractName: 'Italian University',
          folderId: 2
        },
        {
          contractId: 6,
          contractName: 'Ethopian University',
          folderId: 2
        }
      ]
    },
  ]

  activateContract(contract: any) {
    this.selectedContracts.set(contract.contractId, contract);
    this.activeContractId = contract.contractId;
    this.tabList.updateButtonState();
    this.visibleContract = false;
  }

  removeContract(contract: any) {
    this.selectedContracts.delete(contract.contractId);
    setTimeout(() => (this.activeContractId = 0), 0);
    this.tabList.updateButtonState();
  }

  activateFolder(folderId: number) {
    this.activeFolderId = folderId;
  }

  tabChange(index: any) {
    this.activeContractId = index;
    const contract = this.selectedContracts.get(Number(index));
    this.activeFolderId = contract.folderId;
  }

  checkScreenSize() {
    this.isDesktop = window.innerWidth >= 1200;
  }

  toggleDrawer() {
    this.visibleContract = !this.visibleContract;
  }

  submitQuery() {
    const temp = this.queryList;
    this.queryList.push(this.query);
    this.queryList = [...temp];
    this.query = "";
  }

  keyDown(event: KeyboardEvent) {
    if (event.key == 'Enter') {
      this.submitQuery();
    }
  }

  getContractInsights() {
    window.scrollTo({
      top: document.body.scrollHeight,
      behavior: 'smooth'   // or 'auto'
    });
  }
}
