package code.with.sahbaan.contractgenie.Services;

import code.with.sahbaan.contractgenie.RequestDTO.GetContractsRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;

import java.util.List;

public interface ContractService {

    BaseResponse<List<GetContractsResponse>> getAllContracts(GetContractsRequest getContractsRequest) throws Exception;
}
