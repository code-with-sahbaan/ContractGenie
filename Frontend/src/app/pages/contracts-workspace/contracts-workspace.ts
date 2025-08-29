import { Component, HostListener, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { Chip } from 'primeng/chip';
import { TabList, TabsModule } from 'primeng/tabs';
import { DrawerModule } from 'primeng/drawer';

@Component({
  selector: 'app-contracts-workspace',
  imports: [InputTextModule, ButtonModule, AccordionModule, Chip, TabsModule, DrawerModule],
  templateUrl: './contracts-workspace.html',
  styleUrl: './contracts-workspace.css'
})
export class ContractsWorkspace {

  @ViewChild('tablistRef') tabList!: TabList;
  activeContractId: number = 0;
  activeFolderId: number = 0;
  selectedContracts: Set<any> = new Set();
  isDesktop = true;
  visibleContract = false;


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

  activateContract(contract: any) {
    this.selectedContracts.add(contract);
    this.activeContractId = contract.contractId;
    this.tabList.updateButtonState();
    this.visibleContract = false;
  }

  removeContract(contract: any) {
    this.selectedContracts.delete(contract);
    setTimeout(() => (this.activeContractId = 0), 0);
    this.tabList.updateButtonState();
  }

  activateFolder(folderId: number) {
    this.activeFolderId = folderId;
  }

  tabChange(index: any) {
    this.activeContractId = index;
  }

  checkScreenSize() {
    this.isDesktop = window.innerWidth >= 1200;
  }

  toggleDrawer() {
    this.visibleContract = !this.visibleContract;
  }
}
