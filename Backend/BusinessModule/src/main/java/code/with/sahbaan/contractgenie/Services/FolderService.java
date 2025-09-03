package code.with.sahbaan.contractgenie.Services;

import code.with.sahbaan.contractgenie.Entities.Folder;
import code.with.sahbaan.contractgenie.RequestDTO.AddFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.DeleteFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.GetFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.UpdateFolderRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetFolderResponse;

import java.util.List;

public interface FolderService {

    BaseResponse<List<GetFolderResponse>> getAllFolders() throws Exception;

    BaseResponse<List<GetFolderResponse>> updateFolder(UpdateFolderRequest updateFolderRequest) throws Exception;

    BaseResponse<List<GetFolderResponse>> addFolder(AddFolderRequest  addFolderRequest) throws Exception;

    BaseResponse<List<GetFolderResponse>> deleteFolder(DeleteFolderRequest deleteFolderRequest) throws Exception;

    Folder getFolderById(long folderId) throws Exception;
}
