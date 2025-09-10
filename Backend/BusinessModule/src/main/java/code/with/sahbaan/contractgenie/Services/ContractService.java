package code.with.sahbaan.contractgenie.Services;

import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.RequestDTO.AddContractRequest;
import code.with.sahbaan.contractgenie.RequestDTO.DeleteContractRequest;
import code.with.sahbaan.contractgenie.RequestDTO.GetContractsRequest;
import code.with.sahbaan.contractgenie.RequestDTO.UpdateContractRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;

import java.util.List;

public interface ContractService {

    BaseResponse<List<GetContractsResponse>> getAllContracts(GetContractsRequest getContractsRequest) throws Exception;

    BaseResponse<List<GetContractsResponse>> addContract(AddContractRequest addContractRequest) throws Exception;

    BaseResponse<List<GetContractsResponse>> updateContract(UpdateContractRequest updateContractRequest) throws Exception;

    BaseResponse<List<GetContractsResponse>> deleteContract(DeleteContractRequest deleteContractRequest) throws Exception;

    void createEmbeddingFromDoc(String docUrl, String docName, Users users) throws Exception;
}
