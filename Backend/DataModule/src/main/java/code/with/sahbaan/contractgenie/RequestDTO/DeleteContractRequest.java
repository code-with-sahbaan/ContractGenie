package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteContractRequest {

    private long contractId;
    private long folderId;
}
