package code.with.sahbaan.contractgenie.Controllers.v1;

import code.with.sahbaan.contractgenie.RequestDTO.AddContractRequest;
import code.with.sahbaan.contractgenie.RequestDTO.GetContractsRequest;
import code.with.sahbaan.contractgenie.RequestDTO.UpdateContractRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;
import code.with.sahbaan.contractgenie.Services.ContractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contract")
@Slf4j
public class ContractController {

    @Autowired
    private ContractService contractService;

    @PostMapping("v1/getContracts")
    public ResponseEntity<BaseResponse<List<GetContractsResponse>>> getContracts(@RequestBody GetContractsRequest getContractsRequest) throws Exception {
        log.info("Executing getContracts in ContractController");
        return new ResponseEntity<>(contractService.getAllContracts(getContractsRequest), HttpStatus.OK);
    }

    @PostMapping("v1/addContract")
    public ResponseEntity<BaseResponse<List<GetContractsResponse>>> addContract(
            @RequestParam("contractFile") MultipartFile file,
            @RequestParam("contractName") String contractName,
            @RequestParam("folderId") long folderId)
            throws Exception {

        log.info("Executing addContract in ContractController");
        return new ResponseEntity<>(contractService.addContract(
                new  AddContractRequest(
                        contractName,
                        file,
                        folderId
                )
        ), HttpStatus.OK);
    }

    @PostMapping("v1/updateContract")
    public ResponseEntity<BaseResponse<List<GetContractsResponse>>> updateContract(@RequestBody UpdateContractRequest updateContractRequest) throws Exception {

        log.info("Executing updateContract in ContractController");
        return new ResponseEntity<>(contractService.updateContract(updateContractRequest), HttpStatus.OK);
    }
}
