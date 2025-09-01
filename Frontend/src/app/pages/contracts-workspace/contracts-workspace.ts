import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { TabList, TabsModule } from 'primeng/tabs';
import { DrawerModule } from 'primeng/drawer';
import { FormsModule } from '@angular/forms';
import { Dialog } from 'primeng/dialog';
import { AddFolder, FolderService, UpdateFolder } from '../../services/folder.service';
import { UiService } from '../../services/ui.service';
import { finalize } from 'rxjs';
import { logout } from '../../utils/common.util';

@Component({
  selector: 'app-contracts-workspace',
  imports: [InputTextModule, ButtonModule, AccordionModule, TabsModule, DrawerModule, FormsModule, Dialog],
  templateUrl: './contracts-workspace.html',
  styleUrl: './contracts-workspace.css'
})
export class ContractsWorkspace implements OnInit {

  constructor(public folderService: FolderService, public uiService: UiService) { }

  @ViewChild('tablistRef') tabList!: TabList;
  activeContractId: number = 0;
  activeFolderId: number = 0;
  selectedContracts: Map<number, any> = new Map<number, any>();
  isDesktop = true;
  visibleContract = false;
  query: string = "";
  queryList: string[] = [];
  addFolderModal: boolean = false;
  updateFolderModal: boolean = false;
  folderName: string = '';
  updatedFolderName: string = '';
  updateFolderModel: UpdateFolder = {
    id: 0,
    folderName: ''
  }

  ngOnInit(): void {
    setTimeout(() => this.getFolders(), 0);
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }
  folders: any[] = []

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

  addFolder() {
    const payload: AddFolder = {
      folderName: this.folderName
    }

    this.uiService.showSpinner();
    this.folderService
      .addFolder(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("Folder Added Successfully");
          const folders = response.responseBody;
          this.folders = [...folders];
          this.addFolderModal = false;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  updateFolder() {
    this.uiService.showSpinner();
    this.updateFolderModel.folderName = this.updatedFolderName;
    this.folderService
      .updateFolder(this.updateFolderModel)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("Folder Updated Successfully");
          const folders = response.responseBody;
          this.folders = [...folders];
          this.updateFolderModal = false;
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  signOut() {
    logout();
  }

  getFolders() {
    this.uiService.showSpinner();
    this.folderService
      .getFolders()
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess(response.responseMessage);
          const folders = response.responseBody;
          this.folders = [...folders];
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  showUpdateFolderModal(folder: any){
    this.updateFolderModel.id = folder.folderId;
    this.updateFolderModel.folderName = folder.folderName;
    this.updatedFolderName = folder.folderName;
    this.updateFolderModal = true;
  }
}
