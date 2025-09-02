import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { TabList, TabsModule } from 'primeng/tabs';
import { DrawerModule } from 'primeng/drawer';
import { FormBuilder, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { Dialog } from 'primeng/dialog';
import { AddFolder, FolderService, UpdateFolder } from '../../services/folder.service';
import { UiService } from '../../services/ui.service';
import { finalize } from 'rxjs';
import { logout, MAX_FILE_SIZE } from '../../utils/common.util';
import { ChatPrompt } from '../../services/ai.service';
import { Select } from 'primeng/select';
import { FileUpload, UploadEvent } from 'primeng/fileupload';

@Component({
  selector: 'app-contracts-workspace',
  imports: [InputTextModule, ButtonModule, AccordionModule, TabsModule, DrawerModule, FormsModule, Dialog, ReactiveFormsModule, Select, FileUpload],
  templateUrl: './contracts-workspace.html',
  styleUrl: './contracts-workspace.css'
})
export class ContractsWorkspace implements OnInit {

  addFolderForm: FormGroup;
  updateFolderForm: FormGroup;
  aiChatForm: FormGroup;
  addContractForm: FormGroup;

  constructor(public folderService: FolderService, public uiService: UiService, public formBuilder: FormBuilder) {
    this.addFolderForm = formBuilder.group({
      folderName: ['', [Validators.required]],
    });

    this.updateFolderForm = formBuilder.group({
      folderName: ['', [Validators.required]],
      folderId: [0, [Validators.required]]
    });

    this.aiChatForm = formBuilder.group({
      userMessage: ['', [Validators.required]],
    });

    this.addContractForm = formBuilder.group({
      contractFile: [null, [Validators.required]],
      contractName: ['', [Validators.required]],
      folderId: [0, [Validators.required]]
    })
  }

  @ViewChild('tablistRef') tabList!: TabList;
  activeContractId: number = 0;
  activeFolderId: number = 0;
  selectedContracts: Map<number, any> = new Map<number, any>();
  isDesktop = true;
  visibleContract = false;
  query: string = "";
  chatMessages: ChatPrompt[] = [];
  addFolderModal: boolean = false;
  updateFolderModal: boolean = false;
  updatedFolderName: string = '';
  addContractModal: boolean = false;

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

  removeContract(event:MouseEvent, contract: any) {
    event.stopPropagation();
    this.selectedContracts.delete(contract.contractId);
    this.activeContractId = 0;
    this.tabList.updateButtonState();
  }

  activateFolder(folderId: number) {
    this.activeFolderId = folderId;
  }

  tabChange(index: any) {
    this.activeContractId = index;
    if (index > 0) {
      const contract = this.selectedContracts.get(Number(index));
      this.activeFolderId = contract.folderId;  
    }
  }

  checkScreenSize() {
    this.isDesktop = window.innerWidth >= 1200;
  }

  toggleDrawer() {
    this.visibleContract = !this.visibleContract;
  }

  submitQuery() {
    if (this.aiChatForm.invalid) {
      return;
    }
    const temp = this.chatMessages;
    temp.push(this.aiChatForm.value);
    this.chatMessages = [...temp];
    this.aiChatForm.reset();
  }

  getContractInsights() {
    window.scrollTo({
      top: document.body.scrollHeight,
      behavior: 'smooth'   // or 'auto'
    });
  }

  addFolder() {
    if (this.addFolderForm.invalid) {
      return;
    }
    const payload: AddFolder = {
      folderName: this.addFolderForm.get('folderName')?.value
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
    if (this.updateFolderForm.invalid) {
      return;
    }
    this.uiService.showSpinner();
    this.folderService
      .updateFolder(this.updateFolderForm.value)
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

  showUpdateFolderModal(event: MouseEvent, folder: UpdateFolder) {
    event.stopPropagation();
    this.updateFolderForm.get('folderId')?.setValue(folder.folderId);
    this.updateFolderForm.get('folderName')?.setValue(folder.folderName);
    this.updateFolderModal = true;
  }

  addContract() {
    console.log(this.addContractForm.value);
  }

  onUpload(event: any) {
    const file = event.files[0];
    if (file.size > MAX_FILE_SIZE) {
      this.uiService.showError("File Size too Large. Max File Size allowed: 1GB");
      return;
    }
    this.addContractForm.get('contractFile')?.setValue(file);
  }
}