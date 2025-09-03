package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.Contract;
import code.with.sahbaan.contractgenie.Entities.Folder;
import code.with.sahbaan.contractgenie.Repositories.ContractRepository;
import code.with.sahbaan.contractgenie.RequestDTO.AddContractRequest;
import code.with.sahbaan.contractgenie.RequestDTO.DeleteContractRequest;
import code.with.sahbaan.contractgenie.RequestDTO.GetContractsRequest;
import code.with.sahbaan.contractgenie.RequestDTO.UpdateContractRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;
import code.with.sahbaan.contractgenie.Services.ContractService;
import code.with.sahbaan.contractgenie.Services.FolderService;
import code.with.sahbaan.contractgenie.Services.MediaService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl extends GenericServiceImpl<Contract> implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private FolderService  folderService;

    public ContractServiceImpl() {
        super(Contract.class);
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> getAllContracts(GetContractsRequest getContractsRequest) throws Exception {
        try{
            List<GetContractsResponse> getContractsResponseList = contractRepository.getContractsByFolderId(getContractsRequest.getFolderId());
            return new BaseResponse<>("Contracts Fetched Successfully",getContractsResponseList);
        }catch (Exception e){
            throw new Exception("Failed to get Contracts");
        }
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> addContract(AddContractRequest addContractRequest) throws Exception {
        try{
            Contract contract = new Contract();
            BeanUtils.copyProperties(addContractRequest,contract);
            Folder folder = folderService.getFolderById(addContractRequest.getFolderId());
            contract.setFolder(folder);
            contractRepository.save(contract);
            // Returning All Updated contracts for that folder
            GetContractsRequest getContractsRequest = new GetContractsRequest();
            getContractsRequest.setFolderId(addContractRequest.getFolderId());
            return getAllContracts(getContractsRequest);
        } catch (Exception e) {
            throw new Exception("Failed to add Contract");
        }
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> updateContract(UpdateContractRequest updateContractRequest) throws Exception {
        try{
            Contract contract = contractRepository.findById(updateContractRequest.getContractId()).get();
            BeanUtils.copyProperties(updateContractRequest,contract);
            Folder folder = folderService.getFolderById(updateContractRequest.getFolderId());
            contract.setFolder(folder);
            contractRepository.save(contract);
            // Returning All Updated contracts for that folder
            GetContractsRequest getContractsRequest = new GetContractsRequest();
            getContractsRequest.setFolderId(updateContractRequest.getFolderId());
            return getAllContracts(getContractsRequest);
        }catch (Exception e){
            throw new Exception("Failed to update Contract");
        }
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> deleteContract(DeleteContractRequest deleteContractRequest) throws Exception {
        try{
            contractRepository.deleteById(deleteContractRequest.getContractId());
            // Returning All Updated contracts for that folder
            GetContractsRequest getContractsRequest = new GetContractsRequest();
            getContractsRequest.setFolderId(deleteContractRequest.getFolderId());
            return getAllContracts(getContractsRequest);
        }catch (Exception e){
            throw new Exception("Failed to delete Contract");
        }
    }
}
