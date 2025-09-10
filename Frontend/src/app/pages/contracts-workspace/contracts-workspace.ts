import { Component, HostListener, OnInit, ViewChild } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { TabList, TabsModule } from 'primeng/tabs';
import { DrawerModule } from 'primeng/drawer';
import { FormBuilder, FormGroup, FormsModule, Validators, ReactiveFormsModule } from '@angular/forms';
import { Dialog } from 'primeng/dialog';
import { AddFolder, DeleteFolder, FolderService, UpdateFolder } from '../../services/folder.service';
import { UiService } from '../../services/ui.service';
import { finalize } from 'rxjs';
import { logout, MAX_FILE_SIZE } from '../../utils/common.util';
import { AiService, AskAI, ChatPrompt } from '../../services/ai.service';
import { Select } from 'primeng/select';
import { FileUpload, UploadEvent } from 'primeng/fileupload';
import { AddContract, ContractService, DeleteContract, FolderList, GetContracts, UpdateContract, UploadFileResponse } from '../../services/contract.service';
import { SkeletonModule } from 'primeng/skeleton';
import { NgxExtendedPdfViewerModule } from 'ngx-extended-pdf-viewer';
import { MarkdownComponent } from 'ngx-markdown';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contracts-workspace',
  imports: [
    InputTextModule,
    ButtonModule,
    AccordionModule,
    TabsModule,
    DrawerModule,
    FormsModule,
    Dialog,
    ReactiveFormsModule,
    Select,
    FileUpload,
    SkeletonModule,
    NgxExtendedPdfViewerModule,
    MarkdownComponent,
    CommonModule
  ],
  templateUrl: './contracts-workspace.html',
  styleUrl: './contracts-workspace.css'
})
export class ContractsWorkspace implements OnInit {

  addFolderForm: FormGroup;
  updateFolderForm: FormGroup;
  aiChatForm: FormGroup;
  addContractForm: FormGroup;
  updateContractForm: FormGroup;

  constructor(
    public folderService: FolderService,
    public contractService: ContractService,
    public uiService: UiService,
    public formBuilder: FormBuilder,
    public aiService: AiService
  ) {
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
      contractUrl: ['', [Validators.required]],
      contractName: ['', [Validators.required]],
      contractFileName: ['', [Validators.required]],
      folderId: [0, [Validators.required]]
    });

    this.updateContractForm = formBuilder.group({
      contractUrl: ['', [Validators.required]],
      contractName: ['', [Validators.required]],
      contractFileName: ['', [Validators.required]],
      folderId: [0, [Validators.required]],
      contractId: [0, [Validators.required]]
    })
  }

  @ViewChild('tablistRef') tabList!: TabList;
  activeContractId: number = 0;
  activeContractUrl: string = '';
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
  updateContractModal: boolean = false;
  fileUploadLoader: boolean = false;
  chatLoader: boolean = false;
  loadingContracts: { [key: string]: boolean } = {};
  uploadedFile: UploadFileResponse = {
    fileName: '',
    fileUrl: ''
  }

  ngOnInit(): void {
    setTimeout(() => this.initAPICalls(), 0);
  }

  initAPICalls() {
    this.getFolders();
    this.getChatMessages();
  }

  @HostListener('window:resize')
  onResize() {
    this.checkScreenSize();
  }
  folders: FolderList[] = [];

  getChatMessages() {
    this.uiService.showSpinner();
    this.
      aiService.getChatMessages()
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
          setTimeout(() => this.scrollToBottom(), 0 )
        })
      )
      .subscribe({
        next: (response) => {
          const list = response.responseBody;
          this.chatMessages = [...list];
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  activateContract(contract: any) {
    this.selectedContracts.set(contract.contractId, contract);
    this.activeContractId = contract.contractId;
    this.activeContractUrl = contract.contractUrl;
    this.tabList.updateButtonState();
    this.visibleContract = false;
  }

  removeContract(event: MouseEvent, contract: any) {
    event.stopPropagation();
    this.selectedContracts.delete(contract.contractId);
    this.activeContractId = 0;
    this.tabList.updateButtonState();
  }

  activateFolder(folderId: number) {
    if (folderId == this.activeFolderId) {
      this.activeFolderId = 0;
      return;
    }
    this.activeFolderId = folderId;
    this.loadingContracts[folderId] = true;
    const payload: GetContracts = {
      folderId: folderId
    }
    this.contractService
      .getContracts(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
          this.loadingContracts[folderId] = false;
        })
      )
      .subscribe({
        next: (response) => {
          const contracts = response.responseBody;
          this.addContractsToFolder(folderId, contracts);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  tabChange(index: any) {
    this.activeContractId = index;
    if (index > 0) {
      const contract = this.selectedContracts.get(Number(index));
      this.activeFolderId = contract.folderId;
      this.activeContractUrl = contract.contractUrl;
    }else{
      setTimeout(() => this.scrollToBottom(),0)
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
    const payload: AskAI = {
      userPrompt: this.aiChatForm.get('userMessage')?.value
    }
    this.chatLoader = true;
    this.
      aiService.getAiAnswer(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.chatLoader = false;
          setTimeout(() => this.scrollToBottom(), 0 );
        })
      )
      .subscribe({
        next: (response) => {
          const finalPrompt: ChatPrompt = response.responseBody;
          const temp = this.chatMessages;
          temp.push(finalPrompt);
          this.chatMessages = [...temp];
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });

    this.aiChatForm.reset();
  }

  scrollToBottom() {
    let ele = document.getElementById("chatWindow");
    if (ele) {
      ele.scrollTop = ele?.scrollHeight;
    }
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

  showUpdateContractModal(contract: any) {
    this.updateContractModal = true;
    // Setting up values
    this.updateContractForm.setValue(contract);
  }

  addContract() {
    if (this.addContractForm.invalid) {
      return;
    }
    const payload: AddContract = this.addContractForm.value;
    this.uiService.showSpinner();
    this.contractService
      .addContract(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("Contract Added Successfully. You will be emailed when your embeddings will be created and trained by LLM.");
          const contracts = response.responseBody;
          this.addContractModal = false;
          this.addContractsToFolder(this.addContractForm.get('folderId')?.value, contracts);
          this.addContractForm.reset();
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  updateContract() {
    if (this.updateContractForm.invalid) {
      return;
    }
    const payload: UpdateContract = this.updateContractForm.value;
    this.uiService.showSpinner();
    this.contractService
      .updateContract(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("Contract Updated Successfully");
          const contracts = response.responseBody;
          this.updateContractModal = false;
          this.addContractsToFolder(this.updateContractForm.get('folderId')?.value, contracts);
          this.addContractForm.reset();
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  addContractsToFolder(folderId: number, contracts: any[]) {
    let index = 0;
    let folder = null;
    this.activeFolderId = folderId;
    for (let i = 0; i < this.folders.length; i++) {
      if (this.folders[i].folderId == folderId) {
        index = i;
        folder = this.folders[i];
      }
    }

    if (folder) {
      folder.contracts = [...contracts];
      this.folders[index] = folder;
    }

  }

  deleteContract(contract: any) {
    const payload: DeleteContract = {
      folderId: contract.folderId,
      contractId: contract.contractId
    };
    this.uiService.showSpinner();
    this.contractService
      .deleteContract(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("Contract Deleted Successfully");
          const contracts = response.responseBody;
          this.addContractsToFolder(contract.folderId, contracts);
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  deleteFolder(event: MouseEvent, folder: any) {
    event.stopPropagation();
    const payload: DeleteFolder = {
      folderId: folder.folderId,
    };
    this.uiService.showSpinner();
    this.folderService
      .deleteFolder(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.uiService.hideSpinner();
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uiService.showSuccess("Folder Deleted Successfully");
          const folders = response.responseBody;
          this.folders = [...folders];
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }

  onUpload(event: any, formGroup: FormGroup, fileName: string, fileUrl: string) {
    const file = event.files[0];
    if (file.size > MAX_FILE_SIZE) {
      this.uiService.showError("File Size too Large. Max File Size allowed: 1GB");
      return;
    }
    const payload = new FormData();
    payload.append("contractFile", file);
    this.fileUploadLoader = true;
    this.contractService
      .uploadFile(payload)
      .pipe(
        finalize(() => {
          // Hiding Loader after API call completion
          this.fileUploadLoader = false;
        })
      )
      .subscribe({
        next: (response) => {
          // Showing success Toast
          this.uploadedFile = response.responseBody;
          formGroup.get(fileUrl)?.setValue(this.uploadedFile.fileUrl);
          formGroup.get(fileName)?.setValue(this.uploadedFile.fileName);
          this.uploadedFile = {
            fileName: '',
            fileUrl: ''
          }
        },
        error: (error) => {
          // Showing error toast
          this.uiService.showError(error.error.responseMessage);
        },
      });
  }
}