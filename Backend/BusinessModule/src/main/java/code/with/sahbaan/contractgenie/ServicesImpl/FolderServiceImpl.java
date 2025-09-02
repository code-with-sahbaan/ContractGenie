package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.Folder;
import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Repositories.FolderRepository;
import code.with.sahbaan.contractgenie.RequestDTO.AddFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.GetFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.UpdateFolderRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetFolderResponse;
import code.with.sahbaan.contractgenie.Services.FolderService;
import code.with.sahbaan.contractgenie.Services.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderServiceImpl extends GenericServiceImpl<Folder> implements FolderService {

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserService userService;

    public FolderServiceImpl() {
        super(Folder.class);
    }

    @Override
    public BaseResponse<List<GetFolderResponse>> getAllFolders() throws Exception {
        try{
            Users users = userService.getCurrentUser();
            List<GetFolderResponse> folders = folderRepository.getFoldersByUserId(users.getUserId());
            return new BaseResponse<>("Folders Fetched Successfully", folders);
        }catch (Exception e){
            throw new Exception("Failed to get folders");
        }
    }

    @Override
    public BaseResponse<List<GetFolderResponse>> updateFolder(UpdateFolderRequest updateFolderRequest) throws Exception {
        try{
            Folder folder = folderRepository.findById(updateFolderRequest.getFolderId()).get();
            folder.setFolderName(updateFolderRequest.getFolderName());
            folderRepository.save(folder);
            return getAllFolders();
        }catch (Exception e){
            throw new Exception("Failed to update folder");
        }
    }

    @Override
    public BaseResponse<List<GetFolderResponse>> addFolder(AddFolderRequest addFolderRequest) throws Exception {
        Users users = userService.getCurrentUser();
        Folder folder = new Folder();
        folder.setFolderName(addFolderRequest.getFolderName());
        folder.setUsers(users);
        folderRepository.save(folder);
        return getAllFolders();
    }

    @Override
    public Folder getFolderById(long folderId) throws Exception {
        try{
            return folderRepository.findById(folderId).get();
        } catch (Exception e) {
            throw new Exception("Failed to get folder");
        }
    }
}
