package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateContractRequest {

    private long contractId;
    private String contractName;
    private String contractUrl;
    private long folderId;
}
