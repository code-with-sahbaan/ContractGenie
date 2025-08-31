package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.Contract;
import code.with.sahbaan.contractgenie.Repositories.ContractRepository;
import code.with.sahbaan.contractgenie.RequestDTO.GetContractsRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;
import code.with.sahbaan.contractgenie.Services.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl extends GenericServiceImpl<Contract> implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

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
}
